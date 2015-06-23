function ret = IsLeapYear(year)

ret = (mod(year,4) == 0 && (mod(year,100) ~= 0 || mod(year,400) == 0));

end
