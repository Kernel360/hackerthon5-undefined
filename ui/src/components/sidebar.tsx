import { useRef, useState } from 'react';

import {
  BookOpen,
  ChevronDown,
  ChevronUp,
  FileText,
  Folder,
  HelpCircle,
  LayoutDashboard,
  LogIn,
  LogOut,
  Menu,
  MessageSquare,
  PlayCircle,
  Settings,
  Space,
  Tag,
  User
} from 'lucide-react';
import { Link } from 'react-router-dom';

import { useAuth } from '@/contexts/auth';
import { axiosInstance } from '@/lib/axios';

import { Button } from './ui/button';
import { ScrollArea } from './ui/scroll-area';
import { Sheet, SheetContent, SheetTrigger } from './ui/sheet';

interface MenuItem {
  title: string;
  path?: string;
  icon?: React.ReactNode;
  submenu?: MenuItem[];
  requireAuth?: boolean;
}

const menuItems: MenuItem[] = [
  {
    title: '대시보드',
    icon: <FileText className='h-5 w-5' />,
    submenu: [
      { title: 'Today', path: '/metrics/today', icon: <Folder className='h-4 w-4' /> },
    ]
  },
];

export const Sidebar = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [showText, setShowText] = useState(true);
  const [expandedMenus, setExpandedMenus] = useState<string[]>([]);
  const panelRef = useRef<HTMLDivElement>(null);
  const { isAuthenticated, user, logout } = useAuth();

  // 로그인 메뉴는 별도로 관리
  const authMenuItem = {
    title: '로그인',
    path: '/login',
    icon: <LogIn className='h-5 w-5' />
  };

  const authenticatedMenuItems = [
    ...menuItems,
    {
      title: '프로필 & 설정',
      path: '/settings',
      icon: <Settings className='h-5 w-5' />,
      requireAuth: true
    }
  ];

  const menuItemsToDisplay = isAuthenticated ? authenticatedMenuItems : menuItems;

  const toggleSubmenu = (title: string) => {
    setExpandedMenus((prev) =>
      prev.includes(title) ? prev.filter((item) => item !== title) : [...prev, title]
    );
  };

  const handleLogout = async () => {
    try {
      await axiosInstance.post('/api/v1/auth/logout');
      logout();
    } catch (error) {
      console.error('로그아웃 실패:', error);
      logout();
    }
  };

  const MenuItem = ({ item }: { item: MenuItem }) => {
    const hasSubmenu = item.submenu && item.submenu.length > 0;
    const isExpanded = expandedMenus.includes(item.title);

    if (hasSubmenu) {
      return (
        <div>
          <button
            onClick={() => toggleSubmenu(item.title)}
            className='flex w-full items-center rounded-lg px-3 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground'
          >
            <span className={`flex items-center ${!showText ? 'w-full justify-center' : ''}`}>
              {item.icon}
              {showText && (
                <>
                  <span className='ml-3 flex-1'>{item.title}</span>
                  <ChevronDown
                    className={`h-4 w-4 transition-transform ${isExpanded ? 'rotate-180' : ''}`}
                  />
                </>
              )}
            </span>
          </button>
          {isExpanded && showText && (
            <div className='ml-4 mt-1 space-y-1'>
              {item.submenu?.map((subItem: MenuItem) => (
                <Link
                  key={subItem.path}
                  to={subItem.path}
                  className='flex items-center rounded-lg px-3 py-2 text-sm font-medium text-muted-foreground hover:bg-accent hover:text-accent-foreground'
                >
                  {subItem.icon && <span className='mr-2'>{subItem.icon}</span>}
                  <span>{subItem.title}</span>
                </Link>
              ))}
            </div>
          )}
        </div>
      );
    }

    return (
      <Link
        to={item.path}
        className='flex items-center rounded-lg px-3 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground'
      >
        <span className={`flex items-center ${!showText ? 'w-full justify-center' : ''}`}>
          {item.icon}
          {showText && <span className='ml-3'>{item.title}</span>}
        </span>
      </Link>
    );
  };

  const SidebarContent = () => (
    <div className='flex h-[calc(100vh-100px)] flex-col' ref={panelRef}>
      <div className='flex items-center justify-between px-4 py-2'>
        {showText && <h2 className='text-lg font-semibold'>메뉴</h2>}
      </div>

      <nav className='flex-1 space-y-1 px-2'>
        {menuItemsToDisplay.map((item) => (
          <MenuItem key={item.path || item.title} item={item} />
        ))}
      </nav>
      {/* 로그인 메뉴 - 하단 고정 */}
      <div className='border-t p-2'>
        {isAuthenticated ? (
          <div className='space-y-4'>
            <div className='px-4 py-2'>
              <div className='flex items-center gap-2'>
                <img src={user?.avatar} alt='프로필' className='h-8 w-8 rounded-full' />
                <span className='font-medium'>{user?.nickname}</span>
              </div>
            </div>
            <Button
              variant='ghost'
              className='w-full justify-start text-red-500 hover:bg-red-100 hover:text-red-600'
              onClick={handleLogout}
            >
              <LogOut className='h-5 w-5' />
              <span className='ml-2'>로그아웃</span>
            </Button>
          </div>
        ) : (
          <Link
            to={authMenuItem.path}
            className='flex items-center rounded-lg px-3 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground'
          >
            <span className={`flex items-center ${!showText ? 'w-full justify-center' : ''}`}>
              {authMenuItem.icon}
              {showText && <span className='ml-3'>{authMenuItem.title}</span>}
            </span>
          </Link>
        )}
      </div>
    </div>
  );

  return (
    <>
      {/* 모바일 사이드바 */}
      <Sheet open={isOpen} onOpenChange={setIsOpen}>
        <SheetTrigger asChild className='lg:hidden'>
          <Button variant='ghost' size='icon'>
            <Menu className='h-6 w-6' />
          </Button>
        </SheetTrigger>
        <SheetContent className='flex h-full w-64 flex-col p-0' side='left'>
          <ScrollArea className='flex-1'>
            <SidebarContent />
          </ScrollArea>
        </SheetContent>
      </Sheet>

      {/* 데스크톱 사이드바 */}
      <div className='hidden lg:block'>
        <div className='flex h-full flex-col border-r bg-background'>
          <ScrollArea className='flex-1'>
            <SidebarContent />
          </ScrollArea>
        </div>
      </div>
    </>
  );
};
