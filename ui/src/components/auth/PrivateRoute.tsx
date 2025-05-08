import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '@/contexts/auth';

interface PrivateRouteProps {
  children: React.ReactNode;
}

export const PrivateRoute = ({ children }: PrivateRouteProps) => {
  const { isAuthenticated, isLoading } = useAuth();
  const location = useLocation();

  if (!isLoading && !isAuthenticated) {
    // 로그인 후 원래 가려던 페이지로 리다이렉트하기 위해 현재 위치를 state로 전달
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return <>{children}</>;
}; 