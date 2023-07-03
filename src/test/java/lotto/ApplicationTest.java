package lotto;

import camp.nextstep.edu.missionutils.test.NsTest;
import lotto.Domain.BonusNumber;
import lotto.Domain.Lotto;
import lotto.Domain.PurchasePrice;
import lotto.Domain.Winning;
import lotto.Domain.WinningNumber;
import lotto.Domain.WinningNumberSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static lotto.Controller.Controller.convertWinningNumberStringToList;
import static lotto.Controller.Controller.getWinningCount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicationTest extends NsTest {
    private static final String ERROR_MESSAGE = "[ERROR]";

    @DisplayName("구입금액을 형식에 맞지 않게 입력하면 [ERROR] 메시지가 출력된다.")
    @ValueSource(strings = {"1000h", "1001"})
    @ParameterizedTest
    void readPurchasePriceTest1(String testPurchasePrice) {
        assertSimpleTest(() -> {
            runException(testPurchasePrice);
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }

    @DisplayName("구입금액을 형식에 맞지 않게 입력하면 예외가 발생한다.")
    @ValueSource(strings = {"1000h", "1001"})
    @ParameterizedTest
    void readPurchasePriceTest2(String testPurchasePrice) {
        assertThrows(IllegalArgumentException.class,
                () -> new PurchasePrice(testPurchasePrice));
    }

    @DisplayName("구입금액은 1,000에 나누어 떨어지는 수이다.")
    @Test
    void generatePurchaseLotteryCountTest() {
        PurchasePrice purchasePrice = new PurchasePrice("54000");
        assertThat(purchasePrice.getPrice() % 1000).isEqualTo(0);
    }

    @DisplayName("당첨 번호를 형식에 맞지 않게 입력하면 [ERROR] 메시지가 출력된다.")
    @ValueSource(strings = {"1, 2, 3, 4, 5, 6", "d,d,d,d,d,d", "1.2.3.4.5.6"})
    @ParameterizedTest
    void generateWinningNumbersTest1(String testWinningNumbers) {
        assertSimpleTest(() -> {
            runException(testWinningNumbers);
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }

    @DisplayName("당첨 번호를 형식에 맞지 않게 입력하면 예외가 발생한다.")
    @ValueSource(strings = {"1, 2, 3, 4, 5, 6", "d,d,d,d,d,d", "1.2.3.4.5.6"})
    @ParameterizedTest
    void generateWinningNumbersTest2(String testWinningNumbers) {
        assertThrows(IllegalArgumentException.class,
                () -> new WinningNumber(testWinningNumbers));
    }

    @DisplayName("보너스 번호를 형식에 맞지 않게 입력하면 [ERROR] 메시지가 출력된다.")
    @ValueSource(strings = {"0", "46", "h", "0h"})
    @ParameterizedTest
    void generateBonusNumberTest1(String testNumber) {
        assertSimpleTest(() -> {
            runException("1000", "1,2,3,4,5,6", testNumber);
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }

    @DisplayName("보너스 번호를 형식에 맞지 않게 입력하면 예외가 발생한다.")
    @ValueSource(strings = {"0", "46", "h", "0h"})
    @ParameterizedTest
    void generateBonusNumberTest2(String testNumber) {
        assertThrows(IllegalArgumentException.class,
                () -> new BonusNumber(testNumber));
    }

    @DisplayName("보너스 번호가 이미 당첨 번호에 포함되어 있다면 예외가 발생한다.")
    @Test
    void generateWinningNumberSetTest() {
        assertThrows(IllegalArgumentException.class,
                () -> new WinningNumberSet(6, new Lotto(List.of(1, 2, 3, 4, 5, 6))));
    }

    @DisplayName("알맞은 당첨 번호 문자열을 입력하면 정상적으로 리스트가 반환된다.")
    @Test
    void convertWinningNumberStringToListTest() {
        assertThat(convertWinningNumberStringToList("1,2,3,4,5,6")).isEqualTo(List.of(1,2,3,4,5,6));
    }


    @DisplayName("당첨 번호와 로또를 비교하여 일치하는 숫자를 제대로 찾는지 확인")
    @Test
    void getWinningCountTest() {
        assertAll(
                () -> assertEquals(getWinningCount(List.of(1, 2, 3, 4, 5, 6), List.of(7, 8, 9, 10, 11, 12, 13)), 0),
                () -> assertEquals(getWinningCount(List.of(1, 2, 3, 4, 5, 6), List.of(1, 7, 8, 9, 10, 11, 12)), 1),
                () -> assertEquals(getWinningCount(List.of(1, 2, 3, 4, 5, 6), List.of(1, 2, 7, 8, 9, 10, 11)), 2),
                () -> assertEquals(getWinningCount(List.of(1, 2, 3, 4, 5, 6), List.of(1, 2, 3, 7, 8, 9, 10)), 3),
                () -> assertEquals(getWinningCount(List.of(1, 2, 3, 4, 5, 6), List.of(1, 2, 3, 4, 7, 8, 9)), 4),
                () -> assertEquals(getWinningCount(List.of(1, 2, 3, 4, 5, 6), List.of(1, 2, 3, 4, 5, 7, 8)), 5),
                () -> assertEquals(getWinningCount(List.of(1, 2, 3, 4, 5, 6), List.of(1, 2, 3, 4, 5, 6, 7)), 6)
        );
    }

    @DisplayName("당첨 번호와 보너스 번호 포함 유무가 주어질 때 알맞은 메뉴와 일치하는지 확인")
    @Test
    void getRightWinningScoreTest() {
        assertAll(
                () -> assertEquals(Winning.getRightWinningScore(0, false), Winning.NO_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(1, true), Winning.NO_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(1, false), Winning.NO_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(2, true), Winning.NO_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(2, false), Winning.NO_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(3, true), Winning.THREE_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(3, false), Winning.THREE_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(4, true), Winning.FOUR_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(4, false), Winning.FOUR_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(5, true), Winning.FIVE_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(5, false), Winning.FIVE_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(6, true), Winning.FIVE_AND_BONUS_WINNING),
                () -> assertEquals(Winning.getRightWinningScore(6, false), Winning.SIX_WINNING)
        );
    }


    @Test
    void 기능_테스트() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    run("8000", "1,2,3,4,5,6", "7");
                    assertThat(output()).contains(
                            "8개를 구매했습니다.",
                            "[8, 21, 23, 41, 42, 43]",
                            "[3, 5, 11, 16, 32, 38]",
                            "[7, 11, 16, 35, 36, 44]",
                            "[1, 8, 11, 31, 41, 42]",
                            "[13, 14, 16, 38, 42, 45]",
                            "[7, 11, 30, 40, 42, 43]",
                            "[2, 13, 22, 32, 38, 45]",
                            "[1, 3, 5, 14, 22, 45]",
                            "3개 일치 (5,000원) - 1개",
                            "4개 일치 (50,000원) - 0개",
                            "5개 일치 (1,500,000원) - 0개",
                            "5개 일치, 보너스 볼 일치 (30,000,000원) - 0개",
                            "6개 일치 (2,000,000,000원) - 0개",
                            "총 수익률은 62.5%입니다."
                    );
                },
                List.of(8, 21, 23, 41, 42, 43),
                List.of(3, 5, 11, 16, 32, 38),
                List.of(7, 11, 16, 35, 36, 44),
                List.of(1, 8, 11, 31, 41, 42),
                List.of(13, 14, 16, 38, 42, 45),
                List.of(7, 11, 30, 40, 42, 43),
                List.of(2, 13, 22, 32, 38, 45),
                List.of(1, 3, 5, 14, 22, 45)
        );
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            runException("1000j");
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
