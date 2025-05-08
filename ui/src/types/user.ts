export interface User {
  id: number;
  nickname: string;
  avatarUrl: string;
  email?: string;
}

export interface UserProfile {
  user: User | null;
  isAuthenticated: boolean;
}
