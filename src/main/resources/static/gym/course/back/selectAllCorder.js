//會員Modal
function memberModal(userId){
	axios({
		method:'get',
		url:'http://localhost:8080/gymlife/course/cordermember',
		params:{
			'userId':userId
		}
	}).then(res=>{
		$('#memberName').text(res.data.userName);
		memberModalMaker(res.data)
	})
}
function memberModalMaker(data){
	let membertable = document.getElementById('membertable');
	let html = '';
	html += `<div class="card">
							<div class="card-body">
								<ul class="list-group list-group-flush">
									<li class="list-group-item">會員編號:<span
										>${data.userId}</span></li>
									<li class="list-group-item">小名:<span
										>${data.userNickName}</span></li>
									<li class="list-group-item">性別:<span
										>${data.userGender}</span></li>
									<li class="list-group-item">地址:<span
										>${data.userAddress}</span></li>
									<li class="list-group-item">電話:<span
										>${data.userTel}</span></li>
									<li class="list-group-item">E-mail:<span
										>${data.userEmail}</span></li>
								</ul>
								<p class="card-text"></p>
							</div>
						</div>`;
						membertable.innerHTML = html;
}
//更新數量紐
function updateCorderBtn(corderId){
	let updateinput = document.getElementById(corderId);
	updateinput.disabled = false;
	let cost = $('#courseCost-'+corderId).val();
	$(updateinput).on('change',function(e){
	$('#updatebtn-'+corderId).css('display','none');
	$('#updateclick-'+corderId).css('display','block');
	let quantity = $(updateinput).val()
	$('#corderCost-'+corderId).val(quantity*cost);
})
}
//更新訂單數量ajax
function updateCorder(corderId){
	let updateQuantity = document.getElementById(corderId).value
	let corderCost = document.getElementById('corderCost-'+corderId).value
	console.log(updateQuantity);
	Swal.fire({
		title: '確定要更新數量嗎?',
		showDenyButton: true,
		confirmButtonText: '更新',
		denyButtonText: `取消`,
	}).then((result) => {
		if (result.isConfirmed) {
			let timerInterval
Swal.fire({
  title: '正在更新訂單...',
//  html: 'I will close in <b></b> milliseconds.',
  timer: 3500,
  timerProgressBar: true,
  didOpen: () => {
    Swal.showLoading()
    const b = Swal.getHtmlContainer().querySelector('b')
    timerInterval = setInterval(() => {
      b.textContent = Swal.getTimerLeft()
    }, 100)
  },
  willClose: () => {
    clearInterval(timerInterval)
  }
}).then((result) => {
  /* Read more about handling dismissals below */
  if (result.dismiss === Swal.DismissReason.timer) {
    console.log('I was closed by the timer')
  }
})
	axios({
		method:'put',
		url:'http://localhost:8080/gymlife/course/corder/update',
		params:{
			'corderId': corderId,
			'corderQuantity':updateQuantity,
			'corderCost':corderCost
		}
	})
	.then(res=>{
		console.log('res:'+JSON.stringify(res))
//		Swal.fire(
//						'更新成功!',
//						'',
//						'success'
//					)
		AllCorderHtml(res.data);
	})
	} else if (result.isDenied) {
		}
	})
}
//全部corder html
function AllCorderHtml(data){
	let alltable = document.getElementById('alltable');
	let html = '';
	for(i=0;i<data.length;i++){
		console.log(data[i].corderUpdateTime)
		let corderUpdateTime = (data[i].corderUpdateTime === null ? '' : data[i].corderUpdateTime);
	html += 
	`
	<tr>
									<td>${data[i].corderId}</td>
									<td><button class="btn btn-outline-primary btn-sm"
											onclick="memberModal(${data[i].userId})">
											<div data-toggle="modal" data-target="#allmember"
												>${data[i].userId}</div>
										</button></td>
									<td >${data[i].courseName}</td>
									<td >${data[i].corderPayment}</td>
									<td >${data[i].corderTime}</td>
									<td >${corderUpdateTime}</td>
									<td><input id="${data[i].corderId}" class="form-control"type="number" min="1"name="corderQuantity" value="${data[i].corderQuantity}" disabled></td>
									<td ><input id="corderCost-${data[i].corderId}"class="form-control " value="${data[i].corderCost}" disabled></td>
									<td ><p style="color:${data[i].corderState ==0?'black':'red'}">${data[i].corderState ==0?'完成':'待修改'}</p></td>
									<td class="updatebtn"><button
											class="btn btn-outline-primary btn-sm"
											id="updatebtn-${data[i].corderId}"
											onclick="updateCorderBtn(${data[i].corderId})">
											更新</button>
											<input type="hidden" id="courseCost-${data[i].corderId}" value="${data[i].courseCost}">
										<button class="btn btn-outline-primary btn-sm"
											id="updateclick-${data[i].corderId}" onclick="updateCorder(${data[i].corderId})" style="display:none;">提交</button>
									</td>
									<td class="deletebtn">
										<button type="button"
											class="btn btn-outline-danger btn-sm deletechbtn"
											name="coachId"
											onclick="deletecorder(${data[i].corderId})">刪除</button>
									</td>
								</tr>
	`};
	alltable.innerHTML=html;
}
//刪除訂單
function deletecorder(corderId){
	console.log(corderId);
	Swal.fire({
		title: '確定要刪除訂單嗎?',
		showDenyButton: true,
		confirmButtonText: '刪除',
		denyButtonText: `取消`,
	}).then((result) => {
		if (result.isConfirmed) {
	axios({
		method: 'delete',
		url:'http://localhost:8080/gymlife/course/corder/delete',
		params:{
			'corderId':corderId
		}
		})
	.then(res=>{
		Swal.fire(
						'刪除成功!',
						'',
						'success'
					)
		AllCorderHtml(res.data);
	})
	} else if (result.isDenied) {
		}
	})
}