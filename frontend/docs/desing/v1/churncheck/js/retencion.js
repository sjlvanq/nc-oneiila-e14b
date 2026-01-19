// IMPACTO DE ACCIONES
new Chart(document.getElementById("accionesChart"), {
  type: "bar",
  data: {
    labels: ["Descuento", "Congelar", "Sesión gratis", "Seguimiento"],
    datasets: [{
      data: [15,22,10,30],
      backgroundColor: "#22c55e"
    }]
  },
  options: {
    plugins:{legend:{display:false}},
    borderRadius:8
  }
});

// EVOLUCIÓN
new Chart(document.getElementById("evolucionChart"), {
  type: "line",
  data: {
    labels: ["Ene","Feb","Mar","Abr","May","Jun"],
    datasets: [{
      data: [10,14,18,22,28,34],
      borderColor:"#3b82f6",
      backgroundColor:"rgba(59,130,246,.15)",
      fill:true,
      tension:.4
    }]
  },
  options:{
    plugins:{legend:{display:false}}
  }
});
