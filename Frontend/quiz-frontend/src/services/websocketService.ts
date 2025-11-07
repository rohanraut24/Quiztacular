import SockJS from 'sockjs-client';
import { Client, type IMessage } from '@stomp/stompjs';
import { WS_BASE_URL,STORAGE_KEYS } from '../utils/constants';

class WebSocketService {
  private client: Client | null = null;
  private connected: boolean = false;

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  connect(onConnect: () => void, onError: (error: any) => void): void {
    const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
    
    this.client = new Client({
      webSocketFactory: () => new SockJS(WS_BASE_URL),
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      debug: (str) => {
        console.log('STOMP Debug:', str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        this.connected = true;
        console.log('WebSocket connected');
        onConnect();
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame);
        onError(frame);
      },
      onWebSocketError: (event) => {
        console.error('WebSocket error:', event);
        onError(event);
      },
    });

    this.client.activate();
  }

  disconnect(): void {
    if (this.client) {
      this.client.deactivate();
      this.connected = false;
      console.log('WebSocket disconnected');
    }
  }

  subscribe(destination: string, callback: (message: IMessage) => void): void {
    if (this.client && this.connected) {
      this.client.subscribe(destination, callback);
    } else {
      console.error('WebSocket not connected');
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  send(destination: string, body: any): void {
    if (this.client && this.connected) {
      this.client.publish({
        destination,
        body: JSON.stringify(body),
      });
    } else {
      console.error('WebSocket not connected');
    }
  }

  isConnected(): boolean {
    return this.connected;
  }
}

export const websocketService = new WebSocketService();
export default websocketService;