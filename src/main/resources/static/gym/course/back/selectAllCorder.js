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
function updateCorderBtn(corderId){
	let updateinput = document.getElementById(corderId);
	let corderQuantity = document.getElementById('corderQuantity-'+corderId).innerText;
	console.log(corderQuantity)
	let html = '';
	html += `<input type="number" class="form-control input" id="input-${corderId}"name="corderQuantity" value="${corderQuantity}" min="1" step="1" value="1">`;
	updateinput.innerHTML = html;
	$('.input').on('change',function(e){
	$('#updatebtn-'+corderId).css('display','none');
	$('#updateclick-'+corderId).css('display','block');
})
}
function updateCorder(corderId){
	let updateQuantity = document.getElementById('input-'+corderId).value
	console.log(updateQuantity)
	axios({
		method:'put',
		url:'http://localhost:8080/gymlife/course/corder/update',
		params:{
			'corderId': corderId,
			'corderQuantity':updateQuantity
		}
	})
	.then(res=>{
		console.log('res:'+JSON.stringify(res))
	})
}
