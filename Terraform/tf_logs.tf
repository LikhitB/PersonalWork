resource "aws_instance" "logs_instance" {
  ami = data.aws_ami.latest_ami.id
  tags = {
    Name = "LogsInstance"
  }
  instance_type = var.instance_type
}