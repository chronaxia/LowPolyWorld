package com.chronaxia.lowpolyworld.model.entity;

import java.util.List;

/**
 * Created by 一非 on 2018/5/29.
 */

public class ATTResponse {
        private String corpus_no;
        private String err_msg;
        private int err_no;
        private List<String> result;
        private String sn;
        public void setCorpus_no(String corpus_no) {
            this.corpus_no = corpus_no;
        }
        public String getCorpus_no() {
            return corpus_no;
        }

        public void setErr_msg(String err_msg) {
            this.err_msg = err_msg;
        }
        public String getErr_msg() {
            return err_msg;
        }

        public void setErr_no(int err_no) {
            this.err_no = err_no;
        }
        public int getErr_no() {
            return err_no;
        }

        public void setResult(List<String> result) {
            this.result = result;
        }
        public List<String> getResult() {
            return result;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }
        public String getSn() {
            return sn;
        }
}
