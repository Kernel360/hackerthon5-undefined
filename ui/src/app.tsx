import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import ThemeProvider from './providers/theme'
import AppRoutes from './routes'

const queryClient = new QueryClient()

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider>
        <AppRoutes />
      </ThemeProvider>
    </QueryClientProvider>
  )
}

export default App
