% Splits a string into a cell array of substrings, given a separator string.
% INPUTS:
%   str - The input string.
%   sep - The separator string.
% OUTPUT:
%   list - The cell array of substrings.
function list = SplitStr(str,sep)
list = {};
seplen = length(sep);
positions = strfind(str,sep);
pos0 = 1;
for index=1:length(positions)
   pos = positions(index);
   list{index} = str(pos0:pos-seplen);
   pos0 = pos + seplen;
end
list{index+1} = str(pos0:length(str));
end
