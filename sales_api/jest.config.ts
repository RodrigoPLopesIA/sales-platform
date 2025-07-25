const { createDefaultPreset } = require("ts-jest");

const tsJestTransformCfg = createDefaultPreset().transform;

/** @type {import("jest").Config} **/
module.exports = {
  preset: "ts-jest",
  collectCoverage: true,
  coverageDirectory: "coverage",
  coverageProvider: "v8",
};
