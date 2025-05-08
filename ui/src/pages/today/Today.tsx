"use client"

import { Bar, BarChart, Pie, PieChart, Cell, Line, LineChart, XAxis, YAxis, Tooltip, CartesianGrid } from "recharts"
import { Progress } from "@/components/ui/progress"
import { ChartConfig, ChartContainer } from "@/components/ui/chart"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Calendar } from "@/components/ui/calendar"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Button } from "@/components/ui/button"
import { CalendarIcon } from "lucide-react"
import { format, addDays, isToday, isSameDay, isAfter, startOfDay, isValid } from "date-fns"
import { useNavigate, useSearchParams } from "react-router-dom"
import { useState, useEffect } from "react"
import { BarChart as BarChartIcon, LineChart as LineChartIcon } from "lucide-react"
import { ChartCard } from "@/components/ui/ChartCard"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogClose } from "@/components/ui/dialog"

// 24시간 기준 일별 사용시간 데이터
const dailyUsageData = [
  { hour: "00:00", usage: 45 },
  { hour: "01:00", usage: 30 },
  { hour: "02:00", usage: 15 },
  { hour: "03:00", usage: 10 },
  { hour: "04:00", usage: 5 },
  { hour: "05:00", usage: 20 },
  { hour: "06:00", usage: 35 },
  { hour: "07:00", usage: 60 },
  { hour: "08:00", usage: 90 },
  { hour: "09:00", usage: 120 },
  { hour: "10:00", usage: 150 },
  { hour: "11:00", usage: 180 },
  { hour: "12:00", usage: 160 },
  { hour: "13:00", usage: 140 },
  { hour: "14:00", usage: 130 },
  { hour: "15:00", usage: 145 },
  { hour: "16:00", usage: 160 },
  { hour: "17:00", usage: 170 },
  { hour: "18:00", usage: 140 },
  { hour: "19:00", usage: 120 },
  { hour: "20:00", usage: 100 },
  { hour: "21:00", usage: 80 },
  { hour: "22:00", usage: 60 },
  { hour: "23:00", usage: 40 },
]

// 도메인 사용 데이터
const domainData = [
  { name: "github.com", value: 4.5, icon: "🐙" },
  { name: "google.com", value: 3.2, icon: "🔍" },
  { name: "youtube.com", value: 2.8, icon: "📺" },
  { name: "twitter.com", value: 1.5, icon: "🐦" },
  { name: "linkedin.com", value: 1.2, icon: "💼" },
]

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884d8']

const dailyUsageConfig = {
  usage: {
    label: "사용시간",
    color: "#2563eb",
  },
} satisfies ChartConfig

const domainConfig = {
  value: {
    label: "사용시간",
    color: "#2563eb",
  },
} satisfies ChartConfig

// 예시: 도메인별 URL 목록 (실제 데이터 연동 전)
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

const Today: React.FC = () => {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const today = startOfDay(new Date())
  const [date, setDate] = useState<Date>(() => {
    const dateParam = searchParams.get('date')
    const parsed = dateParam ? new Date(dateParam) : today
    return isValid(parsed) ? parsed : today
  })

  const isSelectedToday = isToday(date)

  // 차트 타입 상태
  const [chartType, setChartType] = useState<'bar' | 'line'>('bar')

  // 트리뷰 오픈 상태
  const [openDomain, setOpenDomain] = useState<string | null>(null)

  // 도메인별 최대 사용시간(비율 바 계산용)
  const maxDomainTime = Math.max(...domainData.map(d => d.value))
  // URL별 최대 사용시간(비율 바 계산용, 전체 URL 중 최대)
  const maxUrlTime = Math.max(...Object.values(domainUrlMap).flat().map(u => u.time), 1)

  useEffect(() => {
    // 날짜가 유효하지 않거나 미래면 오늘로
    if (!isValid(date) || isAfter(startOfDay(date), today)) {
      setDate(today)
      return
    }
    const formattedDate = format(date, 'yyyy-MM-dd')
    navigate(`?date=${formattedDate}`, { replace: true })
  }, [date, navigate, today])

  const handlePrev = () => setDate(prev => addDays(prev, -1))
  const handleNext = () => {
    if (!isSelectedToday) setDate(prev => addDays(prev, 1))
  }

  return (
    <div className="container mx-auto p-4 space-y-4">
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between gap-4 flex-wrap">
            <CardTitle>사용 시간 분석</CardTitle>
            <div className="flex items-center gap-2">
              <Button variant="outline" size="icon" onClick={handlePrev} aria-label="이전날">
                {"<"}
              </Button>
              <Popover>
                <PopoverTrigger asChild>
                  <Button variant="outline" className="w-[180px] justify-start text-left font-normal">
                    <CalendarIcon className="mr-2 h-4 w-4" />
                    {date ? format(date, 'PPP') : <span>날짜 선택</span>}
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="end">
                  <Calendar
                    mode="single"
                    selected={date}
                    onSelect={(date) => date && setDate(date)}
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
              <Button variant="outline" size="icon" onClick={handleNext} aria-label="다음날" disabled={isSelectedToday}>
                {">"}
              </Button>
            </div>
          </div>
        </CardHeader>
      </Card>

      <div className="flex flex-col md:flex-row gap-4">
        <div className="md:basis-3/5 w-full">
          <ChartCard
            title="24시간 사용시간"
            actions={
              <>
                <Button
                  variant={chartType === 'bar' ? 'default' : 'outline'}
                  size="icon"
                  aria-label="막대 차트"
                  onClick={() => setChartType('bar')}
                >
                  <BarChartIcon className="w-5 h-5" />
                </Button>
                <Button
                  variant={chartType === 'line' ? 'default' : 'outline'}
                  size="icon"
                  aria-label="선형 차트"
                  onClick={() => setChartType('line')}
                >
                  <LineChartIcon className="w-5 h-5" />
                </Button>
              </>
            }
          >
            {chartType === 'bar' ? (
              <ChartContainer config={dailyUsageConfig} className="h-[300px] w-full">
                <BarChart data={dailyUsageData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="hour" />
                  <YAxis />
                  <Tooltip />
                  <Bar dataKey="usage" fill="#2563eb" radius={4} />
                </BarChart>
              </ChartContainer>
            ) : (
              <ChartContainer config={dailyUsageConfig} className="h-[300px] w-full">
                <LineChart data={dailyUsageData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="hour" />
                  <YAxis />
                  <Tooltip />
                  <Line 
                    type="monotone" 
                    dataKey="usage" 
                    stroke="#2563eb" 
                    strokeWidth={2}
                    dot={{ fill: "#2563eb" }}
                  />
                </LineChart>
              </ChartContainer>
            )}
          </ChartCard>
        </div>
        <div className="md:basis-2/5 w-full">
          <ChartCard title="도메인 사용 비율">
            <ChartContainer config={domainConfig} className="h-[400px] w-full">
              <PieChart>
                <Pie
                  data={domainData}
                  dataKey="value"
                  nameKey="name"
                  cx="50%"
                  cy="50%"
                  outerRadius={150}
                  label={({ name, value }) => `${name} (${value}h)`}
                >
                  {domainData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
              </PieChart>
            </ChartContainer>
          </ChartCard>
        </div>
      </div>

      <ChartCard title="도메인별 사용시간">
        <div className="space-y-2">
          {domainData.map((domain, index) => {
            const isOpen = openDomain === domain.name
            const urlList = domainUrlMap[domain.name] || []
            return (
              <div key={domain.name}>
                {/* 도메인 루트 노드 */}
                <button
                  type="button"
                  tabIndex={0}
                  className={`group w-full flex items-center gap-2 rounded transition ring-2 ring-transparent hover:bg-muted focus:bg-muted focus:ring-primary/40 outline-none px-4 py-2 relative ${isOpen ? 'bg-muted/60 shadow-lg' : ''}`}
                  onClick={() => setOpenDomain(isOpen ? null : domain.name)}
                  onFocus={() => {
                    // TODO: 원하는 포커스 동작 구현
                    console.log('도메인 포커스:', domain)
                  }}
                >
                  {/* 트리 아이콘/라인 */}
                  <span className="w-4 flex justify-center items-center">
                    <svg width="12" height="12" viewBox="0 0 12 12" className={`transition-transform ${isOpen ? 'rotate-90' : ''}`}> <polyline points="4,2 8,6 4,10" fill="none" stroke="#888" strokeWidth="2" strokeLinecap="round"/> </svg>
                  </span>
                  <span className="text-xl">{domain.icon}</span>
                  <span className="font-medium flex-1">{domain.name}</span>
                  {/* 도메인 사용시간 바 */}
                  <span className="relative flex-1 max-w-[200px] h-3 bg-gray-200 rounded overflow-hidden mx-2">
                    <span
                      className="absolute left-0 top-0 h-full bg-primary/80 rounded"
                      style={{ width: `${(domain.value / maxDomainTime) * 100}%` }}
                    />
                  </span>
                  <span className="text-xs text-gray-700 whitespace-nowrap ml-2">{domain.value}시간</span>
                </button>
                {/* URL 자식 노드 */}
                {isOpen && urlList.length > 0 && (
                  <div className="ml-10 mt-1 space-y-1 border-l-2 border-dashed border-gray-300 pl-4">
                    {urlList.map((item, idx) => (
                      <div key={item.url+idx} className="flex items-center gap-2 group">
                        <span className="w-4" />
                        <span className="text-primary">🔗</span>
                        <a href={item.url} target="_blank" rel="noopener noreferrer" className="break-all underline text-blue-600 flex-1">
                          {item.url}
                        </a>
                        <span className="relative flex-1 max-w-[120px] h-2 bg-gray-200 rounded overflow-hidden mx-2">
                          <span
                            className="absolute left-0 top-0 h-full bg-primary/40 rounded"
                            style={{ width: `${(item.time / maxUrlTime) * 100}%` }}
                          />
                        </span>
                        <span className="text-xs text-gray-700 whitespace-nowrap ml-2">{item.time}시간</span>
                      </div>
                    ))}
                  </div>
                )}
                {isOpen && urlList.length === 0 && (
                  <div className="ml-10 py-4 text-gray-400 text-xs">URL 기록이 없습니다.</div>
                )}
              </div>
            )
          })}
        </div>
      </ChartCard>
    </div>
  )
}

export default Today