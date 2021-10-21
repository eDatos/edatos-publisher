package es.gobcan.istac.edatos.publisher.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jhipster-extra", ignoreUnknownFields = false)
public class JHipsterExtraProperties {

	private final Security security = new Security();

	public Security getSecurity() {
		return security;
	}

	public static class Security {

		private final Authentication authentication = new Authentication();

		public Authentication getAuthentication() {
			return authentication;
		}

		public static class Authentication {

			private final Jwt jwt = new Jwt();

			public Jwt getJwt() {
				return jwt;
			}

			public static class Jwt {

				private long tokenRenewPeriodInSeconds = 900;

				public long getTokenRenewPeriodInSeconds() {
					return tokenRenewPeriodInSeconds;
				}

				public void setTokenRenewPeriodInSeconds(long tokenRenewPeriodInSeconds) {
					this.tokenRenewPeriodInSeconds = tokenRenewPeriodInSeconds;
				}
			}

		}

	}
}