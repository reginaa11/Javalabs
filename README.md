# Учебный проект на Java

## Общая информация
- **Автор:** Simonenko Regina
- **Учебная группа:** ФИТ-241
- **Язык программирования:** Java
- **Версия Java:** 21

# SOLID Principles Java Implementation

### SRP 
- `ReportData` - отвечает только за хранение данных отчета
- `ReportManager` - отвечает только за генерацию отчета
- `ReportPrinter` - отвечает только за печать отчета
- `ReportSaver` - отвечает только за сохранение отчета

### OCP 
- `DiscountStrategy` - интерфейс для стратегий скидок
- `RegularDiscount` - обычная скидка 5%
- `VipDiscount` - VIP скидка 15%
- `SuperVipDiscount` - Super VIP скидка 25%
- `DiscountCalculator` - калькулятор, закрытый для модификации

### LSP 
- `Bird` - базовый класс птиц с методом eat()
- `FlyingBird` - наследник с дополнительным методом fly()
- `Penguin` - нелетающая птица (не нарушает поведение базового класса)
