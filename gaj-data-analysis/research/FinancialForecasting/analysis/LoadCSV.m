% Loads CSV data from the file located at the given file path string.
% Assumes the first line is the header containing the field names. 
% Assumes the second line contains the data-types for each field,
% e.g. date, float, long, etc.
%
% INPUT:
%    filepath - String.
% OUTPUT:
%    data - Struct.
function data = LoadData(filepath)

f = fopen(filepath,'r');

header1 = strtrim(fgetline(f));
fieldNames = strrep(SplitStr(header1,','), ' ', '_');
header2 = strtrim(fgetline(f));
dataTypes = SplitStr(header2,',');
if length(dataTypes) ~= length(fieldNames)
   error('Incompatible header lengths!');
end

for idx=1:length(fieldNames)
   switch(dataTypes{idx})
      case {'date', 'string'}
         data.(fieldNames{idx}) = {};
      case {'float', 'integer', 'long'}
         data.(fieldNames{idx}) = [];
      otherwise
         error('Unknown data-type: ' + dataTypes{idx});
   end
end

rowPos = 0;
while 1
   line = strtrim(fgetline(f));
   if isempty(line), break; end
   row = SplitStr(line,',');
   if length(row) ~= length(fieldNames)
      error('Row has wrong length: ' + line);
   end
   rowPos = rowPos + 1;
   for idx=1:length(fieldNames)
      if iscell(data.(fieldNames{idx}))
         data.(fieldNames{idx}){rowPos} = row{idx};
      else
         data.(fieldNames{idx})(rowPos) = eval(row{idx});
      end
   end
end

fclose(f);

end
