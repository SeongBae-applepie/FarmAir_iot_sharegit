// 함수 불러오기 및 bdb 연결
const mysql = require('mysql2');
const express = require('express');
const app = express();
const dbconfig   = require('./config/db_config.js');
const conn = mysql.createConnection(dbconfig);


// 데이터 요청 및 수신
app.get('/',function(req, res){
	conn.connect(); // mysql과 연결

    var sql = 'select * from env'
    conn.query(sql, function(err, rows, fields)
    {
        if (err) {
            console.error('error connecting: ' + err.stack);
        }
        res.send(rows); // rows: 쿼리 실행 결과물
            
    });
    conn.end(); // 연결 해제
}); 
 

// 브라우저에서 http://localhost:5000/ 에 접속하면 위 데이터 조회
app.listen(5000, function(){
	console.log('Listening at 5000');
});


// 새 레코드 삽입 함수
// record1라는 json 레코드가 전송될 때 insertRecord(record1) 구문으로 사용
function insertRecord(record) {
    const query = 'INSERT INTO env (mtime, temp, hum, wtemp, light) VALUES (?, ?, ?, ?, ?)';
    const values = [record.mtime, record.temp, record.hum, record.wtemp, record.light];
  
    conn.query(query, values, (err, results) => {
      if (err) {
        console.error('Error inserting record: ', err);
        return;
      }
      console.log('New record inserted with ID: ', results.insertId);
    });
  }
