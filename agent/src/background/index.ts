

function sendMetric(currentUrl: string) {
    fetch("http://localhost:8080/api/metrics", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        userId: 1,
        url: currentUrl,
        now: new Date().toISOString()
      })
    })
    .then(res => res.json())
    .then(data => console.log("Metric sent:", data))
    .catch(err => console.error("Metric error:", err));
  }
  
  setInterval(() => {
    chrome.tabs.query(
      { active: true, currentWindow: true },
      tabs => {
        if (!tabs[0] || !tabs[0].url) return;
        sendMetric(tabs[0].url);
      }
    );
  }, 1000);

export {};
