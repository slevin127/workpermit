package com.azarin.workpermit.service;

import com.azarin.workpermit.model.PermitParticipant;
import com.azarin.workpermit.model.WorkPermit;
import com.moebiusgames.pdfbox.table.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.moebiusgames.pdfbox.table.PDFRenderContext;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfGeneratorService {
    public byte[] generatePermitHeader(WorkPermit permit) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Подключаем шрифт (добавь файл tahoma.ttf в /resources/fonts)
            PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/fonts/tahoma.ttf"));
            PDFRenderContext ctx = new PDFRenderContext(document, page);
            float margin = 40;
            float currentY = PDRectangle.A4.getHeight() - margin;
            final float LINE_HEIGHT = 14f; // можно отрегулировать под твой шрифт

            // Заголовок
            PDFLabel title = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            title.setText("НАРЯД-ДОПУСК № ________");
            title.getCell().setFont(font).setFontSize(14).setPaddingBottom(5);
            title.render(ctx, margin, PDRectangle.A4.getHeight() - margin);

            PDFLabel subtitle = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            subtitle.setText("на выполнение работ повышенной опасности");
            subtitle.getCell().setFont(font).setFontSize(12).setPaddingBottom(10);
            subtitle.render(ctx, margin, currentY);

            PDFLabel date = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            date.setText("от «____» ____________ 20___ г.");
            date.getCell().setFont(font).setFontSize(12).setPaddingBottom(15);
            date.render(ctx, margin, currentY);

            // Блок реквизитов
            PDFLabel field1 = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            field1.setText("Предприятие, цех, участок _________________________________________________");
            field1.getCell().setFont(font).setFontSize(11).setPaddingBottom(5);
            field1.render(ctx, margin, currentY);

            PDFLabel field2 = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            field2.setText("Организация, выполняющая работы _________________________________________");
            field2.getCell().setFont(font).setFontSize(11).setPaddingBottom(10);
            field2.render(ctx, margin, currentY);


            // === Раздел "1. Наряд" ===
            PDFLabel sectionTitle = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            sectionTitle.setText("1. Наряд");
            sectionTitle.getCell().setFont(font).setFontSize(12).setPaddingBottom(10);
            sectionTitle.render(ctx, margin, currentY);
            currentY -= LINE_HEIGHT;

// 1.1 Руководители работ
            PDFLabel leaders = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            leaders.setText("1. Производителям (руководителям) работ:");
            leaders.getCell().setFont(font).setFontSize(11).setPaddingBottom(5);
            leaders.render(ctx, margin, currentY);
            currentY -= LINE_HEIGHT;

            PDFLabel leaderInfo = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            leaderInfo.setText("(организация, должность, фамилия, инициалы) ____________________________________________");
            leaderInfo.getCell().setFont(font).setFontSize(11).setPaddingBottom(8);
            leaderInfo.render(ctx, margin, currentY);
            currentY -= LINE_HEIGHT;

// 1.2 Состав бригады
            PDFLabel team = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            team.setText("с бригадой:");
            team.getCell().setFont(font).setFontSize(11).setPaddingBottom(4);
            team.render(ctx, margin, currentY);
            currentY -= LINE_HEIGHT;

            PDFLabel teamInfo = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            teamInfo.setText("(должность, фамилия, инициалы членов бригады) __________________________________________");
            teamInfo.getCell().setFont(font).setFontSize(11).setPaddingBottom(8);
            teamInfo.render(ctx, margin, currentY);
            currentY -= LINE_HEIGHT;

// 1.3 Поручается
            PDFLabel task = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            task.setText("поручается:");
            task.getCell().setFont(font).setFontSize(11).setPaddingBottom(4);
            task.render(ctx, margin, currentY);
            currentY -= LINE_HEIGHT;

            PDFLabel taskDesc = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            taskDesc.setText("(указывается точное наименование выполняемых работ)");
            taskDesc.getCell().setFont(font).setFontSize(11).setPaddingBottom(4);
            taskDesc.render(ctx, margin, currentY);
            currentY -= LINE_HEIGHT;

            PDFLabel location = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            location.setText("(указывается место выполнения работ)");
            location.getCell().setFont(font).setFontSize(11).setPaddingBottom(10);
            location.render(ctx, margin, currentY);
            currentY -= LINE_HEIGHT;


            // Шаг: блоки 2–5
            final float SMALL_LINE_HEIGHT = 14f;
            final float BIG_LINE_HEIGHT = 18f;

// 2. Время начала работ
            PDFLabel startTime = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            startTime.setText("2. Работы начать в ___ час. ___ мин. «___» ____________ 20___ г.");
            startTime.getCell().setFont(font).setFontSize(11).setPaddingBottom(5);
            startTime.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

// 3. Время окончания работ
            PDFLabel endTime = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            endTime.setText("3. Работы окончить в ___ час. ___ мин. «___» ____________ 20___ г.");
            endTime.getCell().setFont(font).setFontSize(11).setPaddingBottom(5);
            endTime.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

// 4. Режим работы
            PDFLabel workMode = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            workMode.setText("4. Режим работы __________________________________________");
            workMode.getCell().setFont(font).setFontSize(11).setPaddingBottom(5);
            workMode.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

// 5. Документы, по которым выполняются работы
            PDFLabel docsTitle = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            docsTitle.setText("5. Наименование документов, по которым выполняются работы:");
            docsTitle.getCell().setFont(font).setFontSize(11).setPaddingBottom(3);
            docsTitle.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel docsHint = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            docsHint.setText("(руководство по ремонту, проект производства работ, технологическая карта, инструкция, карта безопасности на повторяющиеся виды работ и др., указывается наименование, номер и дата утверждения документа, организация)");
            docsHint.getCell().setFont(font).setFontSize(9).setPaddingBottom(10);
            docsHint.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

            // === 6. Особые условия труда ===
            PDFLabel specialCondTitle = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            specialCondTitle.setText("6. Особые условия труда");
            specialCondTitle.getCell().setFont(font).setFontSize(11).setPaddingBottom(3);
            specialCondTitle.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel specialCondHint = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            specialCondHint.setText("(указываются основные опасности, наличие вблизи движущихся механизмов, горячих поверхностей, оголённых проводов, находящихся под напряжением, высокая концентрация пыли, газа и т.д.)");
            specialCondHint.getCell().setFont(font).setFontSize(9).setPaddingBottom(10);
            specialCondHint.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;
// === 7. Меры безопасности ===
            PDFLabel safetyTitle = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            safetyTitle.setText("7. Меры безопасности, предусматриваемые дополнительно к учтенным ремонтной документацией:");
            safetyTitle.getCell().setFont(font).setFontSize(11).setPaddingBottom(6);
            safetyTitle.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

// Создаём таблицу
            float tableWidth = PDRectangle.A4.getWidth() - 2 * margin;
            PDFTable table = PDFTable.createWithSomeFixedColumnWidths(
                    tableWidth,
                    40, // № п.п.
                    330, // Меры безопасности
                    PDFTable.AUTO_DETERMINE_COLUMN_WIDTH // Ответственный
            );
            table.setColumnHeadersMode(PDFTable.ColumnHeadersMode.NO_COLUMN_HEADERS);

// Заголовки колонок
            table.getColumn(0).setHeading("№ п.п.");
            table.getColumn(1).setHeading("Установка заземления и ограждений, экранов, дополнительных вентиляторов, вывешивание знаков безопасности, обеспечение СИЗ и др.");
            table.getColumn(2).setHeading("Лицо, ответственное за выполнение (должность, ФИО)\nОтметка о выполнении, подпись исполнителя");

// Пример добавления пустых строк для печати вручную
            for (int i = 0; i < 5; i++) {
                PDFTableRow row = table.addRow();
                row.getCell(0).setContent((i + 1) + ".");
                row.getCell(1).setContent("__________________________________________________________________");
                row.getCell(2).setContent("__________________________________________");
            }

// Рендер таблицы
            table.render(ctx, margin, currentY);
            currentY -= 80; // сдвигаем вниз — примерная высота таблицы (можно подстроить)


// === 8. Приложение к наряду-допуску ===
            PDFLabel appendix = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            appendix.setText("8. Приложение к наряду-допуску (по согласованию заказчика с подрядчиком)");
            appendix.getCell().setFont(font).setFontSize(11).setPaddingBottom(3);
            appendix.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel appendixHint = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            appendixHint.setText("(схемы отключения агрегатов, заглушек, разъемов, установки дополнительных вентиляторов, дополнительного освещения)");
            appendixHint.getCell().setFont(font).setFontSize(9).setPaddingBottom(10);
            appendixHint.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

// === 9. Назначаются допускающими ===
            PDFLabel allowTitle = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            allowTitle.setText("9. Назначаются допускающими к работе:");
            allowTitle.getCell().setFont(font).setFontSize(11).setPaddingBottom(5);
            allowTitle.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel shiftOpen = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            shiftOpen.setText("9.1 Открывающая смена ___________________________________________");
            shiftOpen.getCell().setFont(font).setFontSize(11).setPaddingBottom(4);
            shiftOpen.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel shiftFollow = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            shiftFollow.setText("9.2 Последующие смены __________________________________________");
            shiftFollow.getCell().setFont(font).setFontSize(11).setPaddingBottom(10);
            shiftFollow.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

// === 10. Наряд-допуск выдал ===
            PDFLabel issuedBy = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            issuedBy.setText("10. Наряд-допуск выдал");
            issuedBy.getCell().setFont(font).setFontSize(11).setPaddingBottom(3);
            issuedBy.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel issuedInfo = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            issuedInfo.setText("(должность, фамилия, инициалы, подпись, дата)");
            issuedInfo.getCell().setFont(font).setFontSize(9).setPaddingBottom(15);
            issuedInfo.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;


// === 11. Продление наряда ===
            PDFLabel extended = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            extended.setText("11. Наряд-допуск продлил до ___ час. ___ мин. «___» ____________ 20__ г.");
            extended.getCell().setFont(font).setFontSize(11).setPaddingBottom(4);
            extended.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel extendedInfo = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            extendedInfo.setText("(должность, фамилия, инициалы, подпись, дата)");
            extendedInfo.getCell().setFont(font).setFontSize(9).setPaddingBottom(10);
            extendedInfo.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

// === 12. Получение наряда производителем ===
            PDFLabel received = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            received.setText("12. Наряд-допуск получил производитель (руководитель) работ");
            received.getCell().setFont(font).setFontSize(11).setPaddingBottom(4);
            received.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel receivedInfo = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            receivedInfo.setText("(должность, фамилия, инициалы, подпись, дата)");
            receivedInfo.getCell().setFont(font).setFontSize(9).setPaddingBottom(10);
            receivedInfo.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

// === 13. С нарядом ознакомлены ===
            PDFLabel acquainted = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            acquainted.setText("13. С нарядом ознакомлены:");
            acquainted.getCell().setFont(font).setFontSize(11).setPaddingBottom(4);
            acquainted.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel tableHeader = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            tableHeader.setText("Допускающий к работе                      Производитель (руководитель) работ");
            tableHeader.getCell().setFont(font).setFontSize(10).setPaddingBottom(3);
            tableHeader.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

// Таблица с подписями (по 5 строк)
            for (int i = 0; i < 5; i++) {
                PDFLabel signatureLine = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
                signatureLine.setText(
                        "(фамилия, инициалы, подпись, дата)             (фамилия, инициалы, подпись, дата)"
                );
                signatureLine.getCell().setFont(font).setFontSize(9).setPaddingBottom(3);
                signatureLine.render(ctx, margin, currentY);
                currentY -= SMALL_LINE_HEIGHT;
            }


// === Раздел 2. Допуск ===
            PDFLabel section2 = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            section2.setText("2. Допуск");
            section2.getCell().setFont(font).setFontSize(12).setPaddingBottom(5);
            section2.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

// 1. Инструктаж провел
            PDFLabel instruct1 = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            instruct1.setText("1. Инструктаж производителю (руководителю) работ об особенностях работы в данном ВСП, действующем цехе и непосредственно на месте производства работ провел:");
            instruct1.getCell().setFont(font).setFontSize(10).setPaddingBottom(3);
            instruct1.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel instruct1info = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            instruct1info.setText("(дата, фамилия, инициалы и подпись допускающего к работе)");
            instruct1info.getCell().setFont(font).setFontSize(9).setPaddingBottom(10);
            instruct1info.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

// 2. Инструктаж получил
            PDFLabel instruct2 = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            instruct2.setText("2. Инструктаж от допускающего к работе получил:");
            instruct2.getCell().setFont(font).setFontSize(10).setPaddingBottom(3);
            instruct2.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel instruct2info = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            instruct2info.setText("(дата, фамилия, инициалы и подпись производителя (руководителя) работ)");
            instruct2info.getCell().setFont(font).setFontSize(9).setPaddingBottom(10);
            instruct2info.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

// 3. Инструктаж бригаде
            PDFLabel instruct3 = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            instruct3.setText("3. Инструктаж членам бригады о мерах безопасности ... провел:");
            instruct3.getCell().setFont(font).setFontSize(10).setPaddingBottom(3);
            instruct3.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel instruct3info = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            instruct3info.setText("(дата, фамилия, инициалы и подпись производителя (руководителя) работ)");
            instruct3info.getCell().setFont(font).setFontSize(9).setPaddingBottom(10);
            instruct3info.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

// 4. Меры безопасности обеспечены
            PDFLabel confirmSafety = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            confirmSafety.setText("4. Меры безопасности, указанные в наряде-допуске, обеспечены... Разрешаю приступить к работе:");
            confirmSafety.getCell().setFont(font).setFontSize(10).setPaddingBottom(3);
            confirmSafety.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

// 5. Работы начаты
            PDFLabel startWork = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            startWork.setText("5. Работы начаты в ___ час. ___ мин. «___» ____________ 20___г.");
            startWork.getCell().setFont(font).setFontSize(10).setPaddingBottom(3);
            startWork.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel startWorkSign = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            startWorkSign.setText("Производитель (руководитель) работ: (фамилия, инициалы и подпись)");
            startWorkSign.getCell().setFont(font).setFontSize(9).setPaddingBottom(15);
            startWorkSign.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

// === Таблица "Ежесменное начало и окончание работ" ===
            PDFLabel shiftTableTitle = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            shiftTableTitle.setText("6. Оформление ежесменного начала и окончания работ:");
            shiftTableTitle.getCell().setFont(font).setFontSize(11).setPaddingBottom(5);
            shiftTableTitle.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

// Заголовок таблицы
//            float tableWidth = PDRectangle.A4.getWidth() - 2 * margin;
            PDFTable shiftTable = PDFTable.createWithSomeFixedColumnWidths(
                    tableWidth,
                    20,   // №
                    80,   // дата, время
                    60,   // числ. бригады
                    120,  // допускающий
                    120,  // производитель
                    80,   // дата, время
                    PDFTable.AUTO_DETERMINE_COLUMN_WIDTH // подписи
            );
            shiftTable.setColumnHeadersMode(PDFTable.ColumnHeadersMode.NO_COLUMN_HEADERS);

// Шапка таблицы (одна строка с подписями)
            PDFTableRow headerRow = shiftTable.addRow();
            headerRow.getCell(0).setContent("№");
            headerRow.getCell(1).setContent("Допуск: дата, время");
            headerRow.getCell(2).setContent("Числ. бригады");
            headerRow.getCell(3).setContent("Допускающий (ФИО, подпись)");
            headerRow.getCell(4).setContent("Производитель (ФИО, подпись)");
            headerRow.getCell(5).setContent("Окончание: дата, время");
            headerRow.getCell(6).setContent("Подписи");

// Пустые строки (5 штук)
            for (int i = 0; i < 5; i++) {
                PDFTableRow row = shiftTable.addRow();
                row.getCell(0).setContent(String.valueOf(i + 1));
                row.getCell(1).setContent("__________");
                row.getCell(2).setContent("___");
                row.getCell(3).setContent("_________________________");
                row.getCell(4).setContent("_________________________");
                row.getCell(5).setContent("__________");
                row.getCell(6).setContent("___________________");
            }

// Рендерим таблицу
            shiftTable.render(ctx, margin, currentY);
            currentY -= 100f; // регулировать в зависимости от числа строк

// === 7. Изменения в составе бригады ===
            PDFLabel changesTitle = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            changesTitle.setText("7. Изменения в составе бригады оформляются в приложении № 2 к наряду-допуску.");
            changesTitle.getCell().setFont(font).setFontSize(11).setPaddingBottom(10);
            changesTitle.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

// === Завершение работ ===
            PDFLabel workDone = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            workDone.setText("Работы окончены в ___ час. ___ мин. «___» ____________ 20___г.");
            workDone.getCell().setFont(font).setFontSize(10).setPaddingBottom(5);
            workDone.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel producerSign = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            producerSign.setText("Производитель (руководитель) работ  (подпись, фамилия, инициалы)");
            producerSign.getCell().setFont(font).setFontSize(9).setPaddingBottom(3);
            producerSign.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel allowSign = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            allowSign.setText("Допускающий  (подпись, фамилия, инициалы)");
            allowSign.getCell().setFont(font).setFontSize(9).setPaddingBottom(10);
            allowSign.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

// === Приемка заказчиком ===
            PDFLabel customerAccept = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            customerAccept.setText("Объект из ремонта (строительства, монтажа) принял представитель заказчика:");
            customerAccept.getCell().setFont(font).setFontSize(10).setPaddingBottom(4);
            customerAccept.render(ctx, margin, currentY);
            currentY -= SMALL_LINE_HEIGHT;

            PDFLabel customerInfo = new PDFLabel((int) (PDRectangle.A4.getWidth() - 2 * margin));
            customerInfo.setText("(должность, фамилия, инициалы, подпись, дата)");
            customerInfo.getCell().setFont(font).setFontSize(9).setPaddingBottom(15);
            customerInfo.render(ctx, margin, currentY);
            currentY -= BIG_LINE_HEIGHT;

            // Завершение
            ctx.closeAllPages();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        }
    }


    private void drawText(PDPageContentStream content, String text, float x, float y, int fontSize, PDType0Font font) throws IOException {
        if (text == null) text = "";
        try {
            content.beginText();
            content.setFont(font, fontSize); content.newLineAtOffset(x, y);
            content.showText(text);
            content.endText();
        } catch (Exception e) {
            System.err.println("❌ Ошибка при отрисовке текста: \"" + text + "\" → " + e.getMessage());
            try {
                content.endText(); // попытка безопасно завершить блок
            } catch (Exception ignore) {
            }
        }
    }

}
