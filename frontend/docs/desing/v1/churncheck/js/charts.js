/* ================================
   CHARTS.JS – CHURNCHECK
   Gráficos locales (Canvas API)
   ================================ */

/* Utilidad base para barras */
function drawBarChart(canvasId, values, color = "#22c55e") {
  const canvas = document.getElementById(canvasId);
  if (!canvas) return;

  const ctx = canvas.getContext("2d");
  const w = canvas.width = 400;
  const h = canvas.height = 180;

  ctx.clearRect(0, 0, w, h);
  ctx.fillStyle = "#ffffff";
  ctx.fillRect(0, 0, w, h);

  const max = Math.max(...values);
  const barWidth = 40;
  const gap = 20;

  values.forEach((v, i) => {
    const barHeight = (v / max) * 120;
    const x = 40 + i * (barWidth + gap);
    const y = h - barHeight - 30;

    ctx.fillStyle = color;
    ctx.fillRect(x, y, barWidth, barHeight);

    ctx.fillStyle = "#1e293b";
    ctx.font = "12px Arial";
    ctx.fillText(v, x + 10, y - 5);
  });
}

/* Utilidad para gráfico donut */
function drawDonutChart(canvasId, values, colors) {
  const canvas = document.getElementById(canvasId);
  if (!canvas) return;

  const ctx = canvas.getContext("2d");
  const w = canvas.width = 260;
  const h = canvas.height = 260;
  const cx = w / 2;
  const cy = h / 2;
  const r = 90;

  const total = values.reduce((a, b) => a + b, 0);
  let start = 0;

  values.forEach((v, i) => {
    const slice = (v / total) * Math.PI * 2;
    ctx.beginPath();
    ctx.moveTo(cx, cy);
    ctx.arc(cx, cy, r, start, start + slice);
    ctx.closePath();
    ctx.fillStyle = colors[i];
    ctx.fill();
    start += slice;
  });

  ctx.beginPath();
  ctx.arc(cx, cy, 50, 0, Math.PI * 2);
  ctx.fillStyle = "#ffffff";
  ctx.fill();
}

/* ================================
   DASHBOARD
   ================================ */
drawBarChart("activity", [40, 60, 55, 80, 70]);

/* ================================
   RIESGO
   ================================ */
drawDonutChart(
  "riskChart",
  [24, 52, 20],
  ["#ef4444", "#f59e0b", "#22c55e"]
);

/* ================================
   RETENCIÓN
   ================================ */
drawBarChart(
  "retentionChart",
  [15, 22, 10, 30],
  "#16a34a"
);

/* ================================
   REPORTES
   ================================ */
drawBarChart(
  "monthlyChart",
  [1000, 1050, 1100, 1120, 1150],
  "#0ea5e9"
);

drawDonutChart(
  "distributionChart",
  [1096, 96, 48],
  ["#22c55e", "#f59e0b", "#ef4444"]
);
