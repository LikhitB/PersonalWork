# link of the problem : https://takeuforward.org/plus/dsa/problems/aggressive-cows?source=strivers-a2z-dsa-track
class Solution:
    def canWePlace(self,nums,k,temp):
        cow=1
        place=nums[0]
        for i in range(1,len(nums)):
            if nums[i]-place>=temp:
                place=nums[i]
                cow+=1
            if cow>=k:
                return True
        return False

    def aggressiveCows(self, nums, k):
        nums.sort()
        high=max(nums)-min(nums)
        maxPossible=0
        low=1
        while low<=high:
            mid=(low+high)//2
            if self.canWePlace(nums,k,mid):
                maxPossible=max(maxPossible,mid)
                low=mid+1
            else:
                high=mid-1
        return maxPossible