function out = SMA(in,window)

N = length(in);
M = N - window + 1;
for idx=1:M
   out(idx) = mean(in(idx:window+idx-1));
end

end
