var app = require("express")();ㅈ
var http = require("http").createServer(app);
var io = require("socket.io")(http);

var port = 3000;

io.on("connection", function (socket) {

  //클라이언트 연결되면
  console.log(socket.id, "Connected"); //연결됨을 콘솔에 출력
  
  var id_message = {
    id: `${socket.id} 나의 아이디`,
  };

  //클라이언트에게 값 전달 (amdroid 에서는 log로 띄워짐)
  socket.emit("check_con", id_message);

  //클라이언트에게 msg가 emit되면 실행
  socket.on("msg", function (data) {

    console.log("-------------------");
    console.log(socket.id, data);
    console.log("id: ", socket.id);
    console.log("-------------------");

    //클라이언트에게 전달한 메세지 만들기
    var message = {
      //   hum: `hum : ${data.id}`,
      //   tem: `tem : ${data.id}`,
      //   w_tem: `w_tem : ${data.id}`,
      //   lig: `lig: ${data.id}`,
      hum: `hum : 30`,
      tem: `tem : 25`,
      w_tem: `w_tem : 23`,
      lig: `lig: 4`,
    };

    //클라이언트에게 메세지 전송 
    socket.emit("msg_to_client", message); //클라이언트로 메시지 전송
  });


  socket.on("disconnect", function () {
    //클라이언트 연결 끊어지면 자동 실행
    console.log("disconnected");
  });


});

http.listen(port, () => {
  //클라이언트 대기
  console.log("listening on *:" + port);
});
