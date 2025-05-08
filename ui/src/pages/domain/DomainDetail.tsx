import { useParams, useNavigate } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card"
import { ArrowLeft } from "lucide-react"

// 예시 데이터 (실제 연동 전)
const domainData = [
  { name: "github.com", icon: "🐙" },
  { name: "google.com", icon: "🔍" },
  { name: "youtube.com", icon: "📺" },
  { name: "twitter.com", icon: "🐦" },
  { name: "linkedin.com", icon: "💼" },
]

const domainUrlMap: Record<string, { url: string; time: number }[]> = {
  "github.com": [
    { url: "https://github.com/", time: 1.2 },
    { url: "https://github.com/user/repo1", time: 0.7 },
    { url: "https://github.com/user/repo2/issues", time: 0.3 },
  ],
  "google.com": [
    { url: "https://google.com/", time: 0.5 },
    { url: "https://google.com/search?q=react", time: 1.1 },
  ],
  "youtube.com": [
    { url: "https://youtube.com/", time: 0.2 },
    { url: "https://youtube.com/watch?v=abc123", time: 1.5 },
    { url: "https://youtube.com/watch?v=xyz456", time: 1.1 },
  ],
  "twitter.com": [
    { url: "https://twitter.com/", time: 0.3 },
    { url: "https://twitter.com/user/status/123", time: 1.2 },
  ],
  "linkedin.com": [
    { url: "https://linkedin.com/", time: 0.4 },
    { url: "https://linkedin.com/in/username", time: 0.8 },
  ],
}

export default function DomainDetail() {
  const { domainName } = useParams<{ domainName: string }>()
  const navigate = useNavigate()
  const domain = domainData.find(d => d.name === domainName)
  const urlList = domainUrlMap[domainName || ""] || []

  return (
    <div className="container mx-auto p-4 max-w-2xl">
      <Button variant="ghost" className="mb-4" onClick={() => navigate(-1)}>
        <ArrowLeft className="w-4 h-4 mr-2" /> 뒤로가기
      </Button>
      <Card>
        <CardHeader className="flex flex-col items-center gap-2">
          <span className="text-5xl">{domain?.icon}</span>
          <CardTitle className="text-2xl">{domain?.name}</CardTitle>
          <span className="text-xs text-gray-500">URL별 사용시간 상세</span>
        </CardHeader>
        <CardContent>
          {urlList.length > 0 ? (
            <ul className="space-y-3 mt-2">
              {urlList.map((item, idx) => (
                <li key={item.url+idx} className="flex items-center gap-2 p-3 rounded-lg bg-muted/50 hover:bg-primary/10 transition">
                  <span className="text-primary">🔗</span>
                  <a href={item.url} target="_blank" rel="noopener noreferrer" className="break-all underline text-blue-600 flex-1">
                    {item.url}
                  </a>
                  <span className="text-xs text-gray-700 whitespace-nowrap">{item.time}시간</span>
                </li>
              ))}
            </ul>
          ) : (
            <div className="flex flex-col items-center justify-center py-12 text-gray-400">
              <span className="text-5xl mb-2">😶‍🌫️</span>
              <span className="font-medium">URL 기록이 없습니다.</span>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
} 