package lotto.UI;

import camp.nextstep.edu.missionutils.Console;

public class Input {
    public String inputPurchasePrice() {
        System.out.println("구입금액을 입력해 주세요.");
        return Console.readLine();
    }

    public String inputWinningNumbers() {
        System.out.println("당첨 번호를 입력해주세요.");
        return Console.readLine();
    }

    public String inputBonusNumber() {
        System.out.println("보너스 번호를 입력해 주세요.");
        return Console.readLine();
    }
}