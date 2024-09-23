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

				if (json['success']) {

					var alertBox = $('<div class="custom-alert alert-success alert-dismissible fade show">' + 
                        '<div>' + json['success'] + '</div>' + 
                        '</div>');

                    // Thêm alert vào body hoặc vị trí mong muốn
                    $('body').append(alertBox);

                    // Tự động đóng sau 2 giây
                    setTimeout(function() {
                        alertBox.css('animation', 'fadeOut 1s ease');
                        setTimeout(function() {
                            alertBox.remove();
                        }, 1000); // Xóa alert sau khi fade out
                    }, 2000); 

					$('#cart-total').text(json['total']);
					$('#cart_total_down').text(json['total']);
						toastr.success(json['success'], 'Thêm Sản Phẩm vào Giỏ OK')
						$('#cart').load('/giohang/ajax/get-html');
						//$('#cart-total').load('/');

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

				$('#cart-total').text(json['total']);
				$('#cart_total_down').text(json['total']);
			
				if ( window.location.pathname == '/apps/cart')
					{ 
						var alertBox = $('<div class="custom-alert alert-success alert-dismissible fade show">' + 
							'<div>' + json['success'] + '</div>' + 
							'</div>');
		
						// Thêm alert vào body hoặc vị trí mong muốn
						$('body').append(alertBox);
		
						// Tự động đóng sau 2 giây
						setTimeout(function() {
							alertBox.css('animation', 'fadeOut 1s ease');
							setTimeout(function() {
								location = '/apps/cart';
								alertBox.remove();
							}, 1000); // Xóa alert sau khi fade out
						}, 2000);
					
						
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
		// Hiển thị hộp thoại xác nhận
		if (confirm('Bạn có chắc chắn muốn xóa sản phẩm "' + ten + '" khỏi giỏ hàng không?')) {
			// Nếu người dùng chọn "OK", thực hiện xóa sản phẩm
			$.ajax({
				url: '/giohang/xoa/ajax', 
				type: 'post',
				data: 'id_sanpham=' + id_sanpham + '&ten=' + ten,
				dataType: 'json',
				beforeSend: function() {
					// Trước khi gọi ajax thì hiện loading...
					$('#cart > button').button('loading');
				},
				success: function(json) { 
					$('#cart-total').text(json['total']);
					$('#cart_total_down').text(json['total']);
	
					// Kiểm tra nếu đang ở trang giỏ hàng chi tiết, reload lại trang
					if (window.location.pathname == '/apps/cart') { 
						location = '/apps/cart';
					} else {
						// Tải lại phần nội dung giỏ hàng qua Ajax nếu không ở trang chi tiết
						toastr.success(json['success'], 'Đã xóa sản phẩm khỏi giỏ hàng thành công');
						$('#cart').load('/giohang/ajax/get-html');
					}
				},
				error: function() {
					alert('Đã xảy ra lỗi! Không thể xóa sản phẩm khỏi giỏ hàng.');
				}
			});
		} else {
			// Nếu người dùng chọn "Cancel", không làm gì
			toastr.info('Hủy bỏ hành động xóa sản phẩm.');
		}
	}
	
}