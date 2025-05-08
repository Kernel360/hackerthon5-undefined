import React from 'react';

import { Sidebar } from './sidebar';
import ThemeToggle from './ui/theme-toggle';

export default function Navbar() {
  return (
    <header className='sticky top-0 z-50 w-full shrink-0 border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60'>
      <div className='container flex h-14 items-center'>
        <div className='mr-4 flex'>
          <div className='overflow-hidden lg:hidden'>
            <Sidebar />
          </div>
          <a className='mr-6 flex items-center space-x-2' href='/'>
            <span className='font-bold sm:inline-block'>Project uptime</span>
          </a>
        </div>
        <div className='flex flex-1 items-center justify-end space-x-4'>
          <ThemeToggle />
        </div>
      </div>
    </header>
  );
}

