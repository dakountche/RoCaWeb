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
-- Implements the TokenFinder validator by verifying
-- whether a value of a parameter belong to a dictionary
-- @author Djibrilla Amadou Kountché
-- @version 1.0.0

local message =""
local tokens = {}


---
-- Fill the tokens from the activated rule
local function initialize() 
   local i = 0
   while (m.getvar("TX.token"..i) do
       tokens[i+1] = m.getvar("TX.token"..i
       i = i + 1
   end
end


---
-- Determines whether a value belongs to the tokens
--
local function validate(param)

     if not (param.value in tokens) then 
         message = message .. " " .. param.name .. " "..param.value "\n"
         m.log(2, param.name..','..param.value..','..'0') 
     else
        m.log(2, param.name..','..param.value..','..'1')
        message = nil
end


-- ModSecurity entry point
function main()

   local msg="";
   local params = m.getvars("ARGS",  { "lowercase", "htmlEntityDecode" })
   local name = m.getvar("TX.name")

    for i = 1,#params do

      if(params[i].name == "ARGS:"..name) then
          validate(params[i])
          break
    end
    end
    return msg
end