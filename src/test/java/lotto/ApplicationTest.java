package lotto;

import camp.nextstep.edu.missionutils.test.NsTest;
import lotto.Domain.Lotto;
import lotto.Domain.LottoNumber;
import lotto.Domain.PurchasePrice;
import lotto.Domain.Winning;
import lotto.Domain.WinningLotto;
import lotto.UI.Input;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static lotto.Controller.LottoController.getRateOfReturn;
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
    @ValueSource(ints = {1001, 999, 0})
    @ParameterizedTest
    void readPurchasePriceTest2(int testPurchasePrice) {
        assertThrows(IllegalArgumentException.class,
                () -> new PurchasePrice(testPurchasePrice));
    }

    @DisplayName("구입금액은 1,000에 나누어 떨어지는 수이다.")
    @Test
    void generatePurchaseLotteryCountTest() {
        PurchasePrice purchasePrice = new PurchasePrice(54000);
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
                () -> Input.validateInputStringFormat(testWinningNumbers));
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
    @ValueSource(ints = {0, 46})
    @ParameterizedTest
    void generateBonusNumberTest2(int testNumber) {
        assertThrows(IllegalArgumentException.class,
                () -> new LottoNumber(testNumber));
    }

    @DisplayName("보너스 번호가 이미 당첨 번호에 포함되어 있다면 예외가 발생한다.")
    @Test
    void generateWinningNumberSetTest() {
        assertThrows(IllegalArgumentException.class,
                () -> new WinningLotto(new LottoNumber(6), new Lotto(List.of(1, 2, 3, 4, 5, 6))));
    }

    @DisplayName("알맞은 당첨 번호 문자열을 입력하면 정상적으로 리스트가 반환된다.")
    @Test
    void convertWinningNumberStringToListTest() {
        assertThat(Input.convertStringToNumList("1,2,3,4,5,6")).isEqualTo(List.of(1,2,3,4,5,6));
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
                () -> assertEquals(Winning.getRightWinningScore(3, true), Winning.FIFTH),
                () -> assertEquals(Winning.getRightWinningScore(3, false), Winning.FIFTH),
                () -> assertEquals(Winning.getRightWinningScore(4, true), Winning.FOURTH),
                () -> assertEquals(Winning.getRightWinningScore(4, false), Winning.FOURTH),
                () -> assertEquals(Winning.getRightWinningScore(5, true), Winning.THIRD),
                () -> assertEquals(Winning.getRightWinningScore(5, false), Winning.THIRD),
                () -> assertEquals(Winning.getRightWinningScore(6, true), Winning.SECOND),
                () -> assertEquals(Winning.getRightWinningScore(6, false), Winning.FIRST)
        );
    }

    @DisplayName("총 점수가 집계되었을 때 수익률 계산이 잘 되는지 확인")
    @Test
    void getRateOfReturnTest() {
        int testPurchaseLotteryCount = 5;
        Map<Winning, Integer> testWinningScoreMap = new EnumMap<>(Winning.class);
        for (Winning winning : Winning.values()) {
            testWinningScoreMap.put(winning, 0);
        }
        testWinningScoreMap.put(Winning.FIFTH, 1);
        testWinningScoreMap.put(Winning.THIRD, 1);
        assertThat(getRateOfReturn(testPurchaseLotteryCount, testWinningScoreMap)).isEqualTo("30100.0");
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
