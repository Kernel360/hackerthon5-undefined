import { ReactNode, useState } from "react"
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Maximize2, X } from "lucide-react"
import { Dialog, DialogContent } from "@/components/ui/dialog"

interface ChartCardProps {
  title: string
  children: ReactNode
  actions?: ReactNode
}

export function ChartCard({ title, children, actions }: ChartCardProps) {
  const [fullscreen, setFullscreen] = useState(false)

  const card = (
    <Card className={fullscreen ? "flex flex-col max-h-screen" : undefined}>
      <CardHeader className="flex flex-row items-center justify-between">
        <CardTitle>{title}</CardTitle>
        <div className="flex gap-2">
          {actions}
          {!fullscreen && (
            <Button variant="ghost" size="icon" onClick={() => setFullscreen(true)} aria-label="확대">
              <Maximize2 className="w-5 h-5" />
            </Button>
          )}
        </div>
      </CardHeader>
      <CardContent className={fullscreen ? "flex-1 overflow-auto" : undefined}>{children}</CardContent>
    </Card>
  )

  return (
    <>
      {card}
      <Dialog open={fullscreen} onOpenChange={setFullscreen}>
        <DialogContent className="max-w-5xl w-full p-0 bg-transparent shadow-none">
          <div className="p-2">{card}</div>
        </DialogContent>
      </Dialog>
    </>
  )
} 