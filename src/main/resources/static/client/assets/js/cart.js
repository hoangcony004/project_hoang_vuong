var cart = {
    add: function(id_sanpham, soluong, ten) { 
		$.ajax({
			url: '/giohang/them/ajax',
			type: 'post',
			data: 'id_sanpham=' + id_sanpham + '&soluong=' + (typeof(soluong) != 'undefined' ? soluong : 1) +'&ten='+ten ,
			dataType: 'json',
			beforeSend: function() {
                // Hiện biểu tượng loading
				//$('#cart > button').button('loading');
			},
			success: function(json) {	
					// Ngay sau khi nhận được thông tin phúc đáp/phản hồi của máy chủ
					// về việc yêu cầu thêm mới giỏ hàng thì:
					
					// gỡ bỏ các hộp thoại cảnh báo, hộp thoại thông báo, hộp thoại lỗi
					// hộp thoại cung cấp thông tin, v.v...
				//$('.alert, .text-danger .success, .warning, .attention, .information, .error').remove();

				//$('#cart > button').button('reset');

					// Nếu như máy chủ yêu cầu điều hướng sang trang khác
					// vì cần thêm yêu cầu: màu sản phẩm là gì ? model máy nào ? v.v...
					// khi đó người dùng phải thêm vào nhiều option (lựa chọn) thì hệ thống mới
					// cho thêm mới vào giỏ hàng thực sự.
				// if (json['redirect']) {
				// 		// error products are to be redirected ?
				// 		// example: json['error']['recurring'];
				// 		location = json['redirect'];
				// }
					// Cập nhật lại thông tin về giỏ hàng trên giao diện html
					// sau khi vừa thêm mới sản phẩm
				if (json['success']) {
					alert(' thêm sản phẩm vào giỏ hàng được! Kiểm tra đường dẫn ajax và thử lại.');
						// đoạn html này thông báo thêm giỏ hàng thành công.
						// Thêm đoạn mã html vào trước thẻ cha của thẻ con có id="content"
						// $('#content').parent().before('<div class="alert alert-success"><i class="fa fa-check-circle"></i> ' + json['success'] + '<button type="button" class="close" data-dismiss="alert">&times;</button></div>');				
						// $('.alert-success').fadeOut(10000);
						// cập nhật thông tin số sản phẩm trong giỏ hàng
						$('#cart-total').text(json['total']);
						// cuộn lên đầu trang
						//$('html, body').animate({ scrollTop: 0 }, 'slow');
						// tải lại nội dung html của giỏ hàng bằng (ajax load) lấy từ nguồn: /cart-ajax.php
						// chỉ lấy phần nội dung bên trong phần tử html có id="cart" 
						// sau đó đắp phần html đó vào bên trong phần tử id="cart" của trang hiện tại.
						// $('#cart').load('/cart-ajax.php#cart > *');		
                        // alert(json['success']);
						toastr.success(json['success'], 'Thêm Sản Phẩm vào Giỏ OK')
						
						$('#cart').load('/giohang/ajax/get-html');
						alert(' thêm sản phẩm vào giỏ hàng được! Kiểm tra đường dẫn ajax và thử lại.');
				}
			},
			error: function(){
				alert('Lỗi!-Không thêm sản phẩm vào giỏ hàng được! Kiểm tra đường dẫn ajax và thử lại.');
			}
		});
	},
    update: function(id_sanpham, so_luong, ten){
		// alert("Đang cập nhật "+id_sanpham+" với số lượng: "+so_luong); // OK
		$.ajax({
			url: '/giohang/sua/ajax', 
			type: 'post',
			data: 'id_sanpham=' + id_sanpham +'&ten='+ten+ '&so_luong='+(typeof(so_luong) != 'undefined' ? so_luong : 1),
			dataType: 'json',

			beforeSend: function() {
				$('#cart > button').button('loading');
			},
			success: function(json) {
				//$('#cart > button').button('reset');


				if ( window.location.pathname == '/apps/cart')
					{ 
						// Nếu như đường dẫn hiện tại đang là: http://localhost:6868/giohang/chitiet
						// thì điều hướng sang chính nó để refresh lại giỏ hàng
						// ví dụ: user vừa xóa khỏi giỏ hàng một item.
						location = '/apps/cart';
					} 
				else 
					{
						// tải lại nội dung html của giỏ hàng bằng (ajax load) 
						// chỉ lấy phần nội dung bên trong phần tử html có id="cart" 
						// sau đó đắp phần html đó vào bên trong phần tử id="cart" của trang hiện tại.
						toastr.success(json['success'], 'Thêm Sản Phẩm vào Giỏ OK')
						// toastr.success("Xóa Sản Phẩm", 'Xóa bớt sản phẩm khỏi giỏ hàng rồi, OK');
	
						$('#cart').load('/giohang/ajax/get-html');
				}
			}
		});

    },
	remove: function(id_sanpham, ten) { 

		// Mỗi lần chạy lại phải Ctrl + F5 trình duyệt 
		// @todo: xóa bộ nhớ cache của trình duyeetj 
		$.ajax({
			url: '/giohang/xoa/ajax', 
			type: 'post',
			data: 'id_sanpham=' + id_sanpham + '&ten='+ten,
			dataType: 'json',
			beforeSend: function() {
				// Trước khi gọi ajax thì hiện loading...
				$('#cart > button').button('loading');
			},
			success: function(json) { 

				if ( window.location.pathname == '/apps/cart')
				{ 
					// Nếu như đường dẫn hiện tại đang là: http://localhost:6868/giohang/chitiet
					// thì điều hướng sang chính nó để refresh lại giỏ hàng
					// ví dụ: user vừa xóa khỏi giỏ hàng một item.
					location = '/apps/cart';
				} 
				else 
				{
					// tải lại nội dung html của giỏ hàng bằng (ajax load) 
					// chỉ lấy phần nội dung bên trong phần tử html có id="cart" 
					// sau đó đắp phần html đó vào bên trong phần tử id="cart" của trang hiện tại.
					toastr.success(json['success'], 'Thêm Sản Phẩm vào Giỏ OK')
					// toastr.success("Xóa Sản Phẩm", 'Xóa bớt sản phẩm khỏi giỏ hàng rồi, OK');

                    $('#cart').load('/giohang/ajax/get-html');
				}
			},
			error: function(){
				alert('error!');
			}
		});
	}
}