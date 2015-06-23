figure
plot(AVPData.relDate-min(AVPData.relDate),AVPData.Close,'r');
hold on
plot(JPMData.relDate-min(AVPData.relDate),JPMData.Close,'g');
plot(GMCRData.relDate-min(AVPData.relDate),GMCRData.Close,'b');
xlabel('Timeline (days)')
ylabel('Stock price (dollars)');
legend('AVP','JPM', 'GMCR','location','northwest')
title('Unadjusted closing stock prices')
print('../figures/stock-prices-close.pdf');
print('../figures/stock-prices-close.png');

figure
plot(AVPData.relDate-min(AVPData.relDate),AVPData.Open,'r');
hold on
plot(JPMData.relDate-min(AVPData.relDate),JPMData.Open,'g');
plot(GMCRData.relDate-min(AVPData.relDate),GMCRData.Open,'b');
xlabel('Timeline (days)')
ylabel('Stock price (dollars)');
legend('AVP','JPM', 'GMCR','location','northwest')
title('Unadjusted opening stock prices')
print('../figures/stock-prices-open.pdf');
print('../figures/stock-prices-open.png');

d=AVPData.Close(1:end-1)-AVPData.Close(2:end);
timeline=AVPData.relDate(1:end-1)-min(AVPData.relDate);
figure
plot(timeline,d,'r');
xlabel('Timeline (days)')
ylabel('Price change (dollars)');
title('AVP closing price: return, V(t_{n+1}) - V(t_n)');
print('../figures/avp-price-close-diff.pdf')
print('../figures/avp-price-close-diff.png')
clear d

s=AVPData.Close(1:end-1)./AVPData.Close(2:end)-1;
figure
plot(timeline,s,'r');
xlabel('Timeline (days)')
ylabel('Price change');
title('AVP closing price: relative return, [V(t_{n+1}) - V(t_n)] / V(t_n)');
print('../figures/avp-price-close-simple.pdf')
print('../figures/avp-price-close-simple.png')
clear s

r=log(AVPData.Close(1:end-1)./AVPData.Close(2:end));
figure
plot(timeline,r,'r');
xlabel('Timeline (days)')
ylabel('Price change');
title('AVP closing price: rate of return, log (V(t_{n+1}) / V(t_n))');
print('../figures/avp-price-close-log.pdf')
print('../figures/avp-price-close-log.png')
clear r
clear timeline
