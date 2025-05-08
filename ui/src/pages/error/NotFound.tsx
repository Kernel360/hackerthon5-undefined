export default function NotFoundPage() {
  return (
    <div className="h-screen w-full flex flex-col items-center justify-center">
      <h1 className="text-4xl font-bold mb-4">404</h1>
      <p className="text-xl text-muted-foreground mb-8">페이지를 찾을 수 없습니다</p>
      <a 
        href="/" 
        className="text-primary hover:underline"
      >
        홈으로 돌아가기
      </a>
    </div>
  )
} 