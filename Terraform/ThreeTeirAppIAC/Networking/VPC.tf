resource "aws_vpc" "threeVPC" {
  cidr_block = "10.0.0.0/21"
}

resource "aws_subnet" "privateSubnets" {
  vpc_id     = aws_vpc.threeVPC.id
  for_each   = var.private_cidr
  cidr_block = each.value
  tags = {
    Name = each.key
  }
}

resource "aws_subnet" "publicSubnets" {
  vpc_id     = aws_vpc.threeVPC.id
  cidr_block = var.public_cidr[count.index]
  count      = 2
  tags = {
    Name = var.public_cidr_names[count.index]
  }
}
