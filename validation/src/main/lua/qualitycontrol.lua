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
-- Implementation of the simple quality control
-- abnormal value check
-- @author Djibrilla Amadou Kountché
-- @version 1.0.0


local message = ""
local mean
local std

---
--Initialize the local variables.
--
local function initialise()

  -- Retrieve the values of the statistical model
   mean = m.getvar("TX.mean")
   std  = m.getvar("TX.std")

   if mean == nil or std == nil  then
      m.log(1, "Problem in the rule")
      return nil
   end

   mean  = tonumber(mean)
   std  =  tonumber(std)
end

---
-- validate a parameter and log to the audit log
--
local function validate(param)
  if math.abs(string.len(param.value) - mean) > 3*std then
     message = message .. " " .. param.name .. " "..param.value "\n"
     m.log(2, param.name..','..param.value..','..'0')

  else
     m.log(2, param.name..','..param.value..','..'1')
     message = nil
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
   local paramname = m.getvar("TX.name")

   -- Search the matched variable with name and validate it
   for i=1,#params do
      if(params[i].name == "ARGS:"..paramname) then
         validate(params[i])
         break
      end
   end

  return  message
end
