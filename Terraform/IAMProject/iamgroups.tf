resource "aws_iam_group" "education"{
   name="education"
   path ="/education/"
}
resource "aws_iam_group_membership" "education_membership"{
   users = [for user in aws_iam_user.users : user.name if lower(user.tags.Department) == "education"]
   group= aws_iam_group.education.name
   name   = aws_iam_group.education.name
}
