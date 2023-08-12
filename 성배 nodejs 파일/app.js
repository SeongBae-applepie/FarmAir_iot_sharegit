const express = require("express");
const mysql = require("mysql2");
const dbconfig = require("./config/db_config.js");
const conn = mysql.createConnection(dbconfig);

var http = require("http").createServer(express);
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
    conn.connect();

    var sql = "select * from env order by id desc limit 1";

    conn.query(sql, function (err, rows, fields) {
      console.log(rows);

      var message = {
        hum: rows[0].hum,
        tem: rows[0].temp,
        w_tem: rows[0].wtemp,
        lig: rows[0].light,
      };

      socket.emit("msg_to_client", message);

      if (err) {
        console.error("error connecting: " + err.stack);
      }
    });

    console.log("-------------------");
    console.log(socket.id, data);
    console.log("id: ", socket.id);
    console.log("-------------------");
  });

  socket.on("disconnect", function () {
    //클라이언트 연결 끊어지면 자동 실행
    conn.end(); // 연결 해제
    console.log("disconnected");
  });
});

http.listen(port, () => {
  //클라이언트 대기
  console.log("listening on *:" + port);
});
