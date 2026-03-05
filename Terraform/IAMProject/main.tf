resource "aws_iam_user" "users" {
  for_each = { for user in local.users : user.first_name => user }
  name     = "${substr(upper(each.value.first_name),0,1)}.${each.key}"
  path= "/userdata/"
  tags = {
    Department = each.value.department
    JobTitle   = each.value.job_title
  }
}

resource "aws_iam_user_login_profile" "loginProfile"{
   for_each = aws_iam_user.users
   user = each.value.name
   password_reset_required= true
   password_length = min(10)
  lifecycle {
    ignore_changes = [
      password_length,
      password_reset_required,
      pgp_key,
    ]
  }
}
