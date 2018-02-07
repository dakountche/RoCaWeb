--[[

 This file is part of RoCaWeb.

 RoCaWeb is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>

--]]



-- Implements the KHI-2 validation
-- @author Djibrilla Amadou Kountché 
-- @version 1.0

-- TODO note that this model needs a representative data set otherwise the icd construction will fail

require 'cephes'


local default_alpha = 0.05
local icd = {}
local msg = ""
local expected ={}
local observed = {}
local length = 0



local function printTable(observed)
  local message = "["
  for k,v in ipairs(observed) do
      message = message .. v ..","
  end
  return message .."]"
end


---
-- Sum the values of the table given in parameter between i and j.
--
local function sum(observed, i, j)
   local value = 0

   if observed then
     while observed[i] and i <=j do
        value = value + observed[i]
        i = i+1
     end
   end
   return value
end



-- Create 6 bins for the observed values as  given in Kruegel et al.
local function getBins(observed, expected)
   local values = observed
   local modifiedObserved ={}
   local k = #observed
   local v = #expected
   if k == v then
      return observed
   else
       modifiedObserved[1] = observed[1]
       modifiedObserved[2] = sum(observed, 2, 4)
       modifiedObserved[3] = sum(observed, 5, 7)
       modifiedObserved[4] = sum(observed, 8, 12)
       modifiedObserved[5] = sum(observed, 13, 16)
       modifiedObserved[6] = sum(observed, 17, #observed)

      return modifiedObserved
   end

end


---
--  Khi2 determines the p-value of the
--  return the value of : kh2, distribution value, p-value
local function khi2(observed, expected)
   local khi = 0
   if   #observed ~= #expected then
      -- Log as error
      m.log(1,print('Table size are not equal ' .. #observed .. ' ' .. #expected))
      m.log(1, "Not enough value to do the learning")
   end

   for i = 1, #observed do
      khi = khi + (math.pow((observed[i] - expected[i]),2)/expected[i])
   end
    --Log as debug
    m.log(3,print("khi: " .. khi))
  -- The complementary cumulative distribution

  local pvalue = cephes.chdtrc(#observed-1, khi)

  return khi, cephes.chdtri(#observed - 1, default_alpha), pvalue
end


-- Determine the occurrences of the characters
local function characterOccurences(chaine)
    local pairValues  = {}
    local observed = {}

    -- Count the occurrence of each character
    for i = 1, #chaine do
       local char = chaine:sub(i,i)
       if not pairValues[char]  then
             pairValues[char] = 1
       else
          pairValues[char] = pairValues[char] + 1
       end
     end

     -- Get the occurrence count and sort them
     local i = 1
     for k,v in pairs(pairValues) do
         observed[i] = v
         i = i+1
     end

    table.sort(observed, function(a,b) return a > b end)

    return observed
end



-- Get the character distribution from the rule
local function getICD()
  local expected = {}

  for j=1, 6 do
    local val = m.getvar("TX.icd"..j-1)
    if (val) then expected[j] = tonumber(val) end
  end

  return expected
end


local function initialise()
   length = string.len(params[i].value)
   icd = getICD()
   expected = createExpected(icd, length)


  -- Get the observed values and then compute 6 bins
   observed = characterOccurences(params[i].value)

  observed = getBins(observed,expected)
  if not observed or not expected  then
    m.log(1, "Not enough data to do the validation !")
  end
end


local function validate(param)

  local khi, dist, p_value = khi2(observed,expected)

  if (khi > dist) then
     m.log(1, "\nkhi= "..khi .."\ndist= "..dist .."\n p-value ="..p_value .."\nlen = "..string.len(params[i].value)..  "\n icd= " .. printTable(getICD()) .. "\nobserved=  " .. printTable(observed) .. "\n expected : " .. printTable(expected))
     msg = msg .. param.name .." " .. param.value
     m.log(3, param.name..','..param.value..','..'1')
  else
     m.log(3, param.name..','..param.value..','..'0')
     msg = nil
  end
end

---
-- Create the expected array with is the icd times
-- the length of the current string
local function createExpected(icd, length)
  local expected ={}
   -- Determines the expected values
   for k,v in ipairs(icd) do
      expected[k] = v*length
    end

    return expected
end

-- ModSecurity entry point
function main()

   local msg="";
   local params = m.getvars("ARGS",  { "lowercase", "htmlEntityDecode" })
   local name = m.getvar("TX.name")


    for i = 1,#params do

      if(params[i].name == "ARGS:"..name) then
          validate(param[i])
          break
    end
    end
    return msg
end
