//新增課程
let icbtn = document.getElementById('insertcourse');
icbtn.addEventListener('click', function(e) {
	e.preventDefault();
//	console.log(666);
	let insertform = document.getElementById('insertform');
	let insertobject = new FormData(insertform);
	//	console.log(insertobject);
	Swal.fire({
		title: '確定要新增嗎?',
		showDenyButton: true,
		confirmButtonText: '新增',
		denyButtonText: `取消`,
	}).then((result) => {
		if (result.isConfirmed) {

			axios.post('http://localhost:8080/gymlife/course/insert', insertobject)
				.then(res => {
					//					console.log('upload ok!!!');
										console.log('res' + JSON.stringify(res));
					$('#insertx').click();
					Swal.fire(
						'新增成功!',
						'',
						'success'
					)
					htmlMakerall(res.data)

				})
		} else if (result.isDenied) {
		}
	})
})
//修改課程
let updatecbtn = document.getElementById('updatecbtn');
updatecbtn.addEventListener('click', function(e) {
	e.preventDefault();
//	console.log(666);
	let updateform = document.getElementById('updatecourseform');
	let updateobject = new FormData(updateform);
	//	console.log(insertobject);
	Swal.fire({
		title: '確定要修改嗎?',
		showDenyButton: true,
		confirmButtonText: '修改',
		denyButtonText: `取消`,
	}).then((result) => {
		if (result.isConfirmed) {

			axios.put('http://localhost:8080/gymlife/course/update', updateobject)
				.then(res => {
					
					$('#updatex').click();
					Swal.fire(
						'修改成功!',
						'',
						'success'
					)
					htmlMakerall(res.data)

				})
		} else if (result.isDenied) {
		}
	})
})
//刪除課程
function deletecourse(courseId){
	Swal.fire({
		title: '確定要刪除嗎?',
		showDenyButton: true,
		confirmButtonText: '刪除',
		denyButtonText: `取消`,
	}).then((result) => {
		if (result.isConfirmed) {

			axios({
				method:'delete',
				url:"http://localhost:8080/gymlife/course/delete",
				params:{
					courseId:courseId
				}
			})
			.then(res => {
					Swal.fire(
						'刪除成功!',
						'',
						'success'
					)
					htmlMakerall(res.data)
				})
		} else if (result.isDenied) {
		}
	})
}
//全部課程
function htmlMakerall(data) {
	let alltable = document.getElementById('alltable');
	let html = '';
	html += `<thead>
							<tr>
								<th scope="col">課程編號</th>
								<th scope="col">課程圖片</th>
								<th scope="col">課程</th>
								<th scope="col">教練</th>
								<th scope="col">價格</th>
								<th scope="col">介紹</th>
								<th class="editth" colspan="2">編輯</th>
							</tr>
						</thead>
						<tbody>`;
	for (i = 0; i < data.length; i++) {
		html += `<tr>
									<th scope="row">${data[i].courseId}</th>
									<td><button class="btn btn-outline-primary btn-sm" onclick="CourseImgModal(${data[i].courseId})">
											<div data-toggle="modal"
												data-target="#AllCourseImg">查看</div>
										</button></td>
									<td 
										id="CourseName-${data[i].courseId}">${data[i].courseName}</td>
									<td><button class="btn btn-outline-primary btn-sm" onclick="CoachModal(${data[i].coachId})">
											<div data-toggle="modal"
												data-target="#coachexampleModalCenter"
												>${data[i].coachName}</div>
										</button></td>
									<td id="CoachId-${data[i].courseId}"
										 style="display: none;">${data[i].coachId}</td>
									<td 
										id="CourseCost-${data[i].courseId}">${data[i].courseCost}</td>
									<td 
										id="CourseIntroduce-${data[i].courseId}">${data[i].courseIntroduce}</td>
									<td class="updatebtn"><button
											class="btn btn-outline-primary btn-sm"
											onclick="updateCourse(${data[i].courseId})">
											<div data-toggle="modal" data-target="#staticBackdrop">更新</div>
										</button></td>
									<td class="deletebtn">
										<button type="button" class="btn btn-outline-danger btn-sm" name="courseId"
												onclick="deletecourse(${data[i].courseId})">刪除</button>
									</td>


								</tr>`;


	}
	html += `</tbody>`;
//	console.log(html);
	alltable.innerHTML = html;
}
//教練Modal
function CoachModal(coachId) {
	axios({
		method: 'get',
		url: "http://localhost:8080/gymlife/course/coachbyid",
		params: {
			coachId: coachId
		}
	})
		.then(res => {
//			console.log('res' + JSON.stringify(res));
			document.getElementById('chtext1').innerText = res.data.coachName;
			htmlmakercoach(res.data)

		})
}
//教練Modal-教練card
function htmlmakercoach(data) {
	let coachtable = document.getElementById('coachtable');
	let html = '';
	html += `						<div class="card">
							<img class="card-img-top"
								src="http://localhost:8080/gymlife/coachImage/${data.coachId}"
								alt="Card image cap">
							<div class="card-body">
								<h5 class="card-title">${data.coachNickName}</h5>
								<h5 class="card-title" >${data.coachName}</h5>
								<ul class="list-group list-group-flush">
									<li class="list-group-item">教練編號:<span
										>${data.coachId}</span></li>
									<li class="list-group-item">連絡電話:<span
										>${data.coachPhoneNumber}</span></li>
									<li class="list-group-item">E-mail:<span
										>${data.coachEmail}</span></li>
									<li class="list-group-item">生日:<span
										>${data.coachBirthday}</span></li>
									<li class="list-group-item">身高:<span
										>${data.coachHeight}</span></li>
									<li class="list-group-item">體重:<span
										>${data.coachWeight}</span></li>
									<li class="list-group-item">介紹:<span
										>${data.coachIntroduce}</span></li>
								</ul>
								<p class="card-text"></p>
							</div>
						</div>`;
	coachtable.innerHTML = html;
}
//課程照片Modal
function CourseImgModal(courseId) {
	document.getElementById('cimgcourseId').innerText = courseId;
	document.getElementById('cimgcourseId').value = courseId;
	axios({
		method: 'get',
		url: "http://localhost:8080/gymlife/course/find",
		params: {
			courseId: courseId
		}
	})
		.then(res => {
//			console.log('res' + JSON.stringify(res));
			document.getElementById('ctext1').innerText = res.data.courseName;
			htmlmakercourseimg(res.data)

		})
}
//課程照片輪播
function htmlmakercourseimg(Data) {
	let courseimgtable = document.getElementById('courseimgtable');
	let html = '';
	for (i = 0; i < Data.imageId.length; i++) {
		if (i === 0) {
			html += `
		<div class="carousel-item active">
		<label style="display: flex; justify-content: center;" onclick="deleteimg(${Data.imageId[i]})">
	      <img class="d-block w-100 cimg" src="http://localhost:8080/gymlife/courseImage/${Data.imageId[i]}" alt="First slide">
	      <div class="cimghover">
														<div class="p-3"
															style="border: 3px solid red; border-radius: 50%">
															<i class="fa-solid fa-trash-can fa-xl"
																style="color: #ff0000;"></i>
														</div>

													</div></label>
	    </div>`;

		} else {
			html += `
		<div class="carousel-item">
		<label style="display: flex; justify-content: center;" onclick="deleteimg(${Data.imageId[i]})">
	      <img class="d-block w-100 cimg" src="http://localhost:8080/gymlife/courseImage/${Data.imageId[i]}" alt="First slide">
	      <div class="cimghover">
														<div class="p-3"
															style="border: 3px solid red; border-radius: 50%">
															<i class="fa-solid fa-trash-can fa-xl"
																style="color: #ff0000;"></i>
														</div>

													</div></label>
	    </div>`;
		}
	}
	courseimgtable.innerHTML = html;
	deletehover();
}
//刪除課程照片
function deleteimg(imageId){
	let courseId = document.getElementById('cimgcourseId').innerText;
	console.log('imageId:'+imageId)
	console.log('courseId:'+courseId)
	Swal.fire({
		title: '確定要刪除這張照片嗎?',
		showDenyButton: true,
		confirmButtonText: '刪除',
		denyButtonText: `取消`,
	}).then((result) => {
		if (result.isConfirmed) {
	axios({
		method: 'delete',
		url: "http://localhost:8080/gymlife/course/deleteImg",
		params: {
			imageId: imageId,
			courseId: courseId
		}
	})
		.then(res => {
			Swal.fire(
						'刪除成功!',
						'',
						'success'
					)
			htmlmakercourseimg(res.data)
		})
		} else if (result.isDenied) {
		}
	})
}
//新增課程照片
function submitForm() {
	let insertcimg = document.getElementById('insertcimg');
	console.log(insertcimg)
	let insertcimgobject = new FormData(insertcimg);
	console.log(insertcimgobject)
	Swal.fire({
		title: '確定要新增照片嗎?',
		showDenyButton: true,
		confirmButtonText: '新增',
		denyButtonText: `取消`,
	}).then((result) => {
		if (result.isConfirmed) {

			axios.post('http://localhost:8080/gymlife/course/insertImg', insertcimgobject)
				.then(res => {
					console.log("資料庫照片已更新!!")
					Swal.fire(
						'新增成功!',
						'',
						'success'
					)
					htmlmakercourseimg(res.data)
				})
		} else if (result.isDenied) {
		}
	})
}

//var stompClient = null;
//	var socket = new SockJS('/gymlife/websocket');
//	stompClient = Stomp.over(socket);
//	stompClient.connect({ }, function(frame) {
//		console.log("開啟連線");
////		stompClient.send(destination, {}, message);
//		stompClient.subscribe('/reply/back', function(updatebean) {
//			let corder = updatebean.body
//			console.log(corder);
//			console.log(corder.courseId);
////			var notificationText = notification.body;
////			console.log(notificationText);
////			disconnect();
//		});
//	}, function(error) {
//		console.log("連線失敗：" + error);
//	});
//	function disconnect() {
//		if (stompClient !== null) {
//			stompClient.disconnect();
//		}
//		console.log("斷開連線");
//	}