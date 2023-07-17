let coursebtn = document.getElementsByClassName('coursebtn');
for (i = 0; i < coursebtn.length; i++) {
	var cbtnlisten = coursebtn[i].addEventListener('click', function(e) {
		e.preventDefault();
		let cbtn = this.getAttribute('data-cid');
		console.log('cbtn:' + cbtn);
		findsinglecourse(cbtn)
	})
}
//教練課程-課程轉換
function findsinglecourse(cbtn) {
	axios({
		method: 'Get',
		url: 'http://localhost:8080/gymlife/front/coursesingle/select',
		params: {
			courseId: cbtn
		}
	})
		.then(res => {
			console.log('res: ' + JSON.stringify(res))
			htmlMaker(res.data)
		})
	function htmlMaker(Data) {
		//課程
		let courseName = document.getElementById('courseName');
		let courseIntroduce = document.getElementById('courseIntroduce');
		courseName.innerText = Data.courseName;
		courseIntroduce.innerText = Data.courseIntroduce;
		let courseCost = document.getElementById('courseCost');
		courseCost.innerText = '$ '+Data.courseCost;
		let coursephoto = document.getElementById('coursephoto');
		console.log(Data.courseId)
		document.getElementById('buycourseId').value = Data.courseId;
		let coursephotohtml = '';
		for (i = 0; i < Data.imageId.length; i++) {
			if (i === 0) {
				coursephotohtml += `
		<div class="carousel-item active">
	      <img class="d-block w-100" src="http://localhost:8080/gymlife/courseImage/${Data.imageId[i]}" alt="First slide">
	    </div>`;
			} else {
				coursephotohtml += `
		<div class="carousel-item">
	      <img class="d-block w-100" src="http://localhost:8080/gymlife/courseImage/${Data.imageId[i]}" alt="First slide">
	    </div>`;
			}
		}
		console.log(coursephotohtml)
		coursephoto.innerHTML = coursephotohtml;
		//教練
		let chphoneemail = document.getElementById('chphoneemail');
		let coachphoto = document.getElementById('coachphoto');
		let coachName = document.getElementById('coachName');
		let coachBirthday = document.getElementById('coachBirthday');
		let coachHeight = document.getElementById('coachHeight');
		let coachWeight = document.getElementById('coachWeight');
		let coachIntroduce = document.getElementById('coachIntroduce');
		chphemhtml=`<a href="tel:${Data.coachPhoneNumber}" ><i class="fa-solid fa-phone" style="color: #fefefe;"></i></a>
                                            <a href="mailto:${Data.coachEmail}" ><i class="fa  fa-envelope-o"></i></a>`;
                                            chphoneemail.innerHTML = chphemhtml;
//		console.log(Data.coach)
		coachphoto.innerHTML = `<img  src="http://localhost:8080/gymlife/coachImage/${Data.coachId}">`;
		coachName.innerText = Data.coachName;
		coachBirthday.innerText = getAge(Data.coachBirthday);
		coachHeight.innerText = Data.coachHeight;
		coachWeight.innerText = Data.coachWeight;
		coachIntroduce.innerText = Data.coachIntroduce;
	}
	
}
//出生日期轉成年齡
function getAge(birthday) {
  if (birthday != null) {
    var birthdate = new Date(birthday);
    var now = new Date();
    var diff = now.getTime() - birthdate.getTime();
    var ageDate = new Date(diff);
    return Math.abs(ageDate.getUTCFullYear() - 1970);
  }
  return 0;
}
//客服對話框
//$('#serviceicon').on('click',function(){
//	console.log(666)
//	toggleDialog();
//})
//$('.close-button').on('click',function(){
//	toggleDialog();
//})
//function toggleDialog() {
//    var dialogBox = document.getElementById("dialog-box");
//    dialogBox.style.display = dialogBox.style.display === "block" ? "none" : "block";
//}

//var openButton = document.getElementById("open-button");
//openButton.addEventListener("click", openDialog);
