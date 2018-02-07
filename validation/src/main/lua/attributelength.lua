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



---
-- Implements a validator using the Chebychev inequality
-- @author Djibrilla Amadou Kountché
-- @version 1.0.0


local message = ""
local mean
local std
local threshold

---
--Initialize the local variables.
--
local function initialise()

  -- Retrieve the values of the statistical model
   mean = m.getvar("TX.mean")
   std  = m.getvar("TX.std")
   threshold = m.getvar("TX.threshold")

   if mean == nil or std == nil or threshold == nil then
      m.log(1, "Problem in the rule")
      return nil
   end

   mean  = tonumber(mean)
   std  =  tonumber(std)
   threshold = tonumber(threshold)
end


---
--Determine the value of the Chebychev inequality
local function chebychev(length, mean, std)
  if  length <= mean then
     return 1.0
  else
     return math.pow(std/(length-mean), 2)
  end
end


---
-- Validate one parameter identified by TX.name
--
local function validate(param)
   local distance = chebychev(string.len(param.value), mean, std)

   --The parameter is considered abnormal given the threshold
   if distance < threshold then
            message = message .. "\n\n"
            message = message .. param.name .. ": \n"
            message = message .. "Length = " .. string.len(param.value) .. "\n"
            message = message .. "Chebychev value = " .. " " .. distance .. "\n"
            --Log to the audit the value as abnormal (class 0)
            m.log(2,param.name..","..param.value..",".."1")
    else
           -- Log to the audit log the value as normal
           m.log(2,param.name..","..param.value..",".."0")
           message = nil
    end
end



local function validateAll(params)

  for i =1,#params do
        validate(params[i])
    end
end


---
-- ModSecurity entry point.
--
function main()

   initialise()

   -- Get all the current values of the request
   local params = m.getvars("ARGS",  { "lowercase", "htmlEntityDecode" })

   -- Get the name of the matched_var
   local paramename = m.getvar("TX.name")

   -- Search the matched variable with name and validate it
   for i=1,#params do
      if(params[i].name == "ARGS:"..paramename) then
         validate(params[i])
         break
      end
   end

  return  message
end
