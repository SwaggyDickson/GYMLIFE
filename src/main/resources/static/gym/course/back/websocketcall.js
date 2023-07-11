$(document).ready(function() {
	selectmail();
})
//查詢email
function selectmail() {
	axios.get('http://localhost:8080/gymlife/mail/select')
		.then(res => {
			console.log('res:' + JSON.stringify(res.data));
			let data = res.data;
			let mailitem = document.getElementById('mailitem');
			let mailhtml = '';
			let notread = 0;
			for (i = 0; i < data.length; i++) {
				if (data[i].mailNotRead == 1) {
					notread++;
				}
				mailhtml += `<a href="#" class="dropdown-item notify-item">                                    
                                    <p class="notify-details ml-0">
                                        <b>課程訂單</b>
                                        <span data-toggle="tooltip" data-placement="bottom"title="${data[i].mail}">${data[i].mail}</span>
                                    </p>
                                </a>
`;
			}
			if (notread >= 1) {
				let mail = document.getElementsByClassName('mail')[0];
				mail.innerHTML = `<span class="notif-bullet"></span>`;
			}
			let noread = document.getElementById('noread');
			noread.textContent = notread;
			mailitem.innerHTML = mailhtml;
		})
}
//接收更新課程訂單資訊
var stompClient = null;
var socket = new SockJS('/gymlife/websocket');
stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
	console.log("開啟連線");
	//		stompClient.send(destination, {}, message);
	stompClient.subscribe('/reply/back', function(updatebean) {
		let corder = JSON.parse(updatebean.body)
		console.log(corder);
		console.log(corder.courseId);
		//		let mail = document.getElementsByClassName('mail')[0];
		//		console.log(mail);
		//信箱跑出紅點
		//		mail.innerHTML = `<span class="notif-bullet"></span>`;
		//		let noread = document.getElementById('noread');
		//未讀訊息+1
		//		noread.textContent = parseInt(noread.textContent) + 1;
		let data = '訂單編號:' + corder.corderId + ',更改課程數量:' + corder.corderQuantity + ',訂單總額:' + corder.corderCost;
		insertMail(data);
		selectmail();
	});
}, function(error) {
	console.log("連線失敗：" + error);
});
function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	console.log("斷開連線");
}
//新增訊息
function insertMail(mail) {
	axios({
		url: 'http://localhost:8080/gymlife/mail/insert',
		method: 'post',
		params: {
			'mail': mail
		}
	})

}