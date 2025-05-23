package com.ytempest.tool.base.mvp;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public interface IModel {
    <T extends IContract> void setContract(T contract);

    <T extends IContract> T getContract();

    void detach();
}
