import React, { useState } from 'react';

import { Button } from './ui/button';
import { Card, CardContent, CardHeader } from './ui/card';

export default function Counter() {
  const [count, setCount] = useState(0);

  return (
    <Button data-testid='theme-toggle' variant='outline'>
      123456
      </Button>
  );
}
