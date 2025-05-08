import React, { createContext, useContext, useState } from 'react';

type LayoutType = 'grid' | 'list';

interface LayoutContextType {
  layout: LayoutType;
  toggleLayout: () => void;
}

const LayoutContext = createContext<LayoutContextType | undefined>(undefined);

export function LayoutProvider({ children }: { children: React.ReactNode }) {
  const [layout, setLayout] = useState<LayoutType>('grid');

  const toggleLayout = () => {
    setLayout(prev => (prev === 'grid' ? 'list' : 'grid'));
  };

  return (
    <LayoutContext.Provider value={{ layout, toggleLayout }}>
      {children}
    </LayoutContext.Provider>
  );
}

export function useLayout() {
  const context = useContext(LayoutContext);
  if (context === undefined) {
    throw new Error('useLayout must be used within a LayoutProvider');
  }
  return context;
} 