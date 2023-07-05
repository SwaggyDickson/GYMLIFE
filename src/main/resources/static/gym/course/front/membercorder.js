function updateCoach(corderId) {

testWebSocketConnection();

}

 function testWebSocketConnection() {
  let socket = new WebSocket('ws://localhost:8080/gymlife/websocket');

  socket.onopen = function() {
    console.log('WebSocket连接已建立');
    var message = '更新订单信息';
    socket.send(message);

    // 后台收到通知后的处理逻辑
    socket.onmessage = function(event) {
        var notification = event.data;
        console.log('收到后台通知:', notification);
        // 在前端执行相应的处理逻辑
    };

    socket.close();
};


  socket.onclose = function() {
    console.log('WebSocket连接已关闭');
  };
}