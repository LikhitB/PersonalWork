# An ARN (Amazon Resource Name) is the unique identifier for any resource in AWS.
## Components of IAM 
| Component | Purpose                                         |
| --------- | ----------------------------------------------- |
| User      | Identity for a person or application            |
| Group     | Collection of users                             |
| Role      | Temporary identity assumed by services or users |
| Policy    | JSON document defining permissions              |

## IAM policy
**Policy can be attached to the following**
- ROLE
- USER
- GROUP

# Example policy:

{ 
   "version":10.2.3.4
   "statement":[{
       "Effect":
       "Action":
       "Resource":
    }]
} 

# Most used classes
-------------------
1. `aws_iam_policy`
2. `aws_iam_users`
3. `aws_iam_group` 
4. `aws_iam_role`
5. `aws_iam_group_membership` 
6. `aws_iam_group_policy` → ** we could create policy for group directly but ARN will not be available for policy in it.
7. `aws_iam_user_policy`  →  **When we want to directly attach to a user**
8. `aws_iam_group_policy_attachment` → **takes the name of the group and arn of the policy**
9. `aws_iam_role_policy_attachment` -> **attach policy to a role**
10. `aws_iam_instance_profile` -> used to attach the role 
11. `aws sts get-caller-identity` -> used to check the if the role is assigned to it or not, example result
```json
    {
    "UserId": "AROAUML2ZJNVNLKABLFRQ:i-06b1fb8ac76c8fbcc",
    "Account": "301442091882",
    "Arn": "arn:aws:sts::301442091882:assumed-role/ec2AccessS3/i-06b1fb8ac76c8fbcc"
    }
```


## Example for policy creation and attaching to a group
```terraform
resource "aws_iam_group_policy_attachment" "attach_policy"{
  group=aws_iam_group.education.name
  policy_arn = aws_iam_policy.education_policy.arn
}

  resource "aws_iam_policy" "education_policy"{
  name="tempPolicy"
  policy=jsondecode({
  Version = ""
  Statment = [{
  Action = [""]
  Effect =""
  Resource = ""
  }]
})
}
```