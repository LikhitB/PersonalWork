resource "aws_iam_group" "education" {
  name = "education"
  path = "/education/"
}
resource "aws_iam_group_membership" "education_membership" {
  users = [for user in aws_iam_user.users : user.name if lower(user.tags.Department) == "education"]
  group = aws_iam_group.education.name
  name  = aws_iam_group.education.name
}

resource "aws_iam_group_policy" "policies" {
  name  = "tempPolicy"
  group = aws_iam_group.education.name
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "s3:ListAllMyBuckets",
        ]
        Effect   = "Allow"
        Resource = "*"
      },
    ]
  })
}

#IAM ROLES DEMO
resource "aws_instance" "demo" {
  ami           = data.aws_ami.amiDetails.id
  instance_type = "t3.micro"
  subnet_id     = aws_subnet.public_subnet.id
  vpc_security_group_ids = [aws_security_group.public_sg.id]
  iam_instance_profile   = aws_iam_instance_profile.instance_profile.name
}

resource "aws_iam_instance_profile" "instance_profile" {
  name = "myfirstProfile"
  role = aws_iam_role.ec2AccessS3.name
}
resource "aws_iam_role" "ec2AccessS3" {
  name = "ec2AccessS3"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "ec2.amazonaws.com"
      }
    }]
  })
}
resource "aws_iam_role_policy_attachment" "attach_policy_to_role" {
  role       = aws_iam_role.ec2AccessS3.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess"
}

# Create a new VPC (if not already present)
resource "aws_vpc" "public_vpc" {
  cidr_block = "10.0.0.0/16"
}

# Create a new public subnet
resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.public_vpc.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "ap-south-1a"
}

# Create an internet gateway
resource "aws_internet_gateway" "public_gw" {
  vpc_id = aws_vpc.public_vpc.id
}

# Create a public route table
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.public_vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.public_gw.id
  }
}

# Associate the route table with the subnet
resource "aws_route_table_association" "public_assoc" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_rt.id
}

# Create a public security group
resource "aws_security_group" "public_sg" {
  name        = "public_sg"
  description = "Allow SSH and HTTP inbound traffic"
  vpc_id      = aws_vpc.public_vpc.id

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
