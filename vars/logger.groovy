def call (String logginglevel = "warning"){
      this.loggingLevel =(evn.PIPELINE_LOGGING_LEVEL) ? evn.PIPELINE_LOGGING_LEVEL : LoggingLevel
      retrun this
}
