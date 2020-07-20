<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" type="text/css"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"">
</head>
<style type="text/css">
body {
	margin: 0px;
}
</style>
<body>
	<table border="0" width="50%"
		style="margin: auto; padding: 30px; background-color: #F3F3F3; border: 1px solid #f37423;">
		<tr>
			<td>
				<table border="0" width="100%">
					<tr>
						<td>
							<h1>
								<img src="cid:${lakeSideImage}" alt="lakeside-ss" height="100px" width="150px">
							</h1>
						</td>
						<td>
							<p style="text-align: right;">
								<a href="${lakeSideURL}" target="_blank"
									style="text-decoration: none;">View In Website</a>
							</p>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" style="text-align:center;width:100%;background-color: #fff;">
					<tr>
						<td style="background-color:#f37423;height:100px;font-size:50px;color:#fff;">
						 <img src="cid:${emailOrangeImage}" alt="email" height="100px" width="150px">						
						</td>
					</tr>
					<tr>
						<td>
							<h1 style="padding-top: 25px;">Your order is on the way!!!</h1>
						</td>
					</tr>
					<tr>
						<td>
							<p style="padding: 0px 100px; text-align: justify;">Hi! Thank you for shopping
								with us! We Will deliver your order on <b>${dayDelivery}</b> between <b>${startHour} -	${endHour}</b><br/><br/> 
								The invoice is attached for your review. Please note the total on the invoice.<br/><br/> 								
								Please remember that there is a <b>$${chargePesos} pesos</b> charge if there is no one to receive your order.<br/><br/> 
								We try very	hard to arrive during the specified time, but we can be a little early or late sometimes Thanks!!</p>
						</td>
					</tr>					
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border="0" width="100%"
					style="border-radius: 5px; text-align: center;">
					<tr>
						<td>
							<h3 style="margin-top: 10px;">Stay in touch</h3>
						</td>
					</tr>
					<tr>
						<td>
							<div style="margin-top: 10px;">
								<img alt="email" src="cid:${emailYellowImage}" height="72px" width="72px"/>							
							</div>
							<a href="${lakeSideEmail}">${lakeSideEmail}</a>
							<br>
							<div style="margin-top: 10px;">
								${personToCall} -   Tel: ${cellPhoneNumber}							
							</div>
														
						</td>						
					</tr>
					<tr>
						<td>
							<div style="margin-top: 20px;">
								<span style="font-size: 12px;">Lake Side Service Shopping Copyright &#169; ${actualYear}</span>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>