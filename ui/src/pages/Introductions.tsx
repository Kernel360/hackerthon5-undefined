import { Button } from "@/components/ui/button";
import { Github, ArrowRight } from "lucide-react";

export default function Introductions() {
  return (
    <div className="min-h-screen flex flex-col justify-center items-center bg-white px-4">
      <div className="max-w-2xl w-full text-center space-y-6 py-8">
        <div className="space-y-4">
          <h1 className="text-6xl font-extrabold tracking-tight text-blue-900 drop-shadow-sm">uptime</h1>
          <p className="text-2xl text-blue-700 font-medium">하루를 추적해보세요</p>
        </div>
        <div className="flex flex-col sm:flex-row justify-center gap-4 mt-8">
          <Button
            variant="default"
            size="lg"
            className="flex items-center justify-center gap-2 text-lg"
            onClick={() => window.location.href = '/metrics/today'}
          >
            시작하기
            <ArrowRight className="w-5 h-5" />
          </Button>
         
        </div>
      </div>
      <footer className="absolute bottom-4 left-0 w-full text-center text-xs text-gray-400">
        <span>© 2025 uptime. All rights reserved.</span>
      </footer>
    </div>
  );
} 