//新增教練
let ichbtn = document.getElementById('insertcoach');
ichbtn.addEventListener('click', function(e) {
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

			axios.post('http://localhost:8080/gymlife/course/coach/insert', insertobject)
				.then(res => {
					console.log('upload ok!!!');
//					console.log('res' + JSON.stringify(res));
					$('#insertx').click();
					Swal.fire(
						'新增成功!',
						'',
						'success'
					)
					htmlMaker(res.data)

				})
		} else if (result.isDenied) {
		}
	})
})

function htmlMaker(data) {
	let alltable = document.getElementById('alltable');
	let html = '';
	html += `<thead>
							<tr>
								<th scope="col">編號</th>
								<th scope="col">姓名</th>
								<th scope="col">小名</th>
								<th scope="col">教練圖片</th>
								<th scope="col">手機號碼</th>
								<th scope="col">E-mail</th>
								<th scope="col">出生日期</th>
								<th scope="col">身高</th>
								<th scope="col">體重</th>
								<th scope="col">介紹</th>
								<th class="editth" colspan="2">編輯</th>
							</tr>
						</thead>
						<tbody>`;
	for (i = 0; i < data.length; i++) {
		html += `<tr>
			<th scope="row">${data[i].coachId}</th>
			<td id="CoachName-${data[i].coachId}">${data[i].coachName}</td>
			<td id="CoachNickName-${data[i].coachId}">${data[i].coachNickName}</td>
			<td>
				<button class="btn btn-outline-primary btn-sm chimgbtn" onclick="CoachImg(${data[i].coachId})" data-chimgId="${data[i].coachId}">
											<div data-toggle="modal"
												data-target="#coachexampleModalCenter">查看</div>
										</button>
			</td>
			<td id="CoachPhoneNumber-${data[i].coachId}">${data[i].coachPhoneNumber}</td>
			<td id="CoachEmail-${data[i].coachId}">${data[i].coachEmail}</td>
			<td id="CoachBirthday-${data[i].coachId}">${data[i].coachBirthday}</td>
			<td id="CoachHeight-${data[i].coachId}">${data[i].coachHeight}</td>
			<td id="CoachWeight-${data[i].coachId}">${data[i].coachWeight}</td>
			<td id="CoachIntroduce-${data[i].coachId}">${data[i].coachIntroduce}</td>

			<td class="updatebtn"><button class="btn btn-outline-primary btn-sm"
				onclick="updateCoach(${data[i].coachId})">
				<div data-toggle="modal" data-target="#staticBackdrop">更新</div>
			</button></td>
			<td class="deletebtn">
					<button type="button" class="btn btn-outline-danger btn-sm" name="coachId"
								onclick="deletecoach(${data[i].coachId})"				>刪除</button>
			</td>
		</tr>`;


	}
	html += `</tbody>`;
	//	console.log(html);
	alltable.innerHTML = html;
}
//更換教練圖片
let CoachImgbtn = document.getElementById('CoachImgbtn');
CoachImgbtn.addEventListener('click', function(e) {
	e.preventDefault();
	let changeimgform = document.getElementById('changeimgform');
	let imgformobject = new FormData(changeimgform);
	Swal.fire({
		title: '確定要更換照片嗎?',
		showDenyButton: true,
		confirmButtonText: '更換',
		denyButtonText: `取消`,
	}).then((result) => {
		if (result.isConfirmed) {

			axios.put('http://localhost:8080/gymlife/course/updatecoachImg', imgformobject)
				.then(res => {
					console.log("資料庫照片已更新!!")
					$('#CoachImgbtn').hide()
					Swal.fire(
						'更換成功!',
						'',
						'success'
					)
					htmlMaker(res.data)
				})
		} else if (result.isDenied) {
		}
	})
})

//修改課程
let updatecoach = document.getElementById('updatecoach');
updatecoach.addEventListener('click', function(e) {
	e.preventDefault();
	let updatecoachform = document.getElementById('updatecoachform');
	let updatechobject = new FormData(updatecoachform);
	Swal.fire({
		title: '確定要修改嗎?',
		showDenyButton: true,
		confirmButtonText: '修改',
		denyButtonText: `取消`,
	}).then((result) => {
		if (result.isConfirmed) {

			axios.put('http://localhost:8080/gymlife/course/coach/update', updatechobject)
				.then(res => {
					$('#updatex').click();
					Swal.fire(
						'修改成功!',
						'',
						'success'
					)
					htmlMaker(res.data)

				})
		} else if (result.isDenied) {
		}
	})
})
//刪除課程
function deletecoach(coachId){
	Swal.fire({
		title: '確定要刪除嗎?',
		showDenyButton: true,
		confirmButtonText: '刪除',
		denyButtonText: `取消`,
	}).then((result) => {
		if (result.isConfirmed) {

			axios({
				method:'delete',
				url:"http://localhost:8080/gymlife/course/coach/delete",
				params:{
					coachId:coachId
				}
			})
			.then(res => {
					Swal.fire(
						'刪除成功!',
						'',
						'success'
					)
					htmlMaker(res.data)
				})
		} else if (result.isDenied) {
		}
	})
}
