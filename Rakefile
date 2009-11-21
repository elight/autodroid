require 'rubygems'
require 'rake'
require 'rexml/document'
require 'rake/clean'

fail "need SCALA_HOME set" unless ENV['SCALA_HOME']
fail "need ANDROID_SDK set" unless ENV['ANDROID_SDK']

def manifest
  @parsed ||= begin
    doc = REXML::Document.new(File.read('AndroidManifest.xml'))
    {
      :package => doc.root.attribute('package').to_s
    }
  end
end

app_pkg = manifest[:package]
project = app_pkg.gsub(/\./, '_')

@spec_pkg = "com.tiggerpalace.campfire.specs"

sdk_location = ENV['ANDROID_SDK']
src = 'src'
gen = 'gen'
res = 'res'
bin = 'bin'
libs = 'libs'
assets = 'assets'
@classes = classes = "#{bin}/classes"
classes_jar = "/System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Classes/classes.jar"
scala_jars  = %w(scala-compiler.jar scala-library.jar).map { |l| File.join(ENV['SCALA_HOME'], 'lib', l) }
test_jars = FileList.new + Dir.open("test_libs") { |dir| 
  dir.collect { |f| File.expand_path("test_libs/#{f}") if File.file?("test_libs/#{f}") }
}.compact

ap_ = "#{bin}/#{project}.ap_"
apk = "#{bin}/#{project}.apk"

android_jar = "#{sdk_location}/platforms/android-2.0/android.jar"
android_aidl = "#{sdk_location}/platforms/android-2.0/framework.aidl"
intermediate_dex_location = "#{bin}/classes.dex"

directory gen
directory bin
directory classes
dirs = [gen, bin, classes]

CLEAN.include(gen, bin)
CLASSPATH = FileList["#{libs}/**/*.jar"] + scala_jars + test_jars
BOOTCLASSPATH = FileList[android_jar, classes_jar]

# Extensions for standard rake classes.
module Rake
  class FileList
    def to_cp(sep = File::PATH_SEPARATOR)
      self.join(sep)
    end
  end
end

def compile_java(dest, *srcdirs)
  files = FileList.new
  srcdirs.each do |d|
    files.include("#{d}/**/*.java")
  end

  unless files.empty?
    sh "javac", "-target", "1.5", "-g", "-bootclasspath", BOOTCLASSPATH.to_cp,  "-nowarn", "-Xlint:none", 
       "-sourcepath", srcdirs.join(File::PATH_SEPARATOR), "-d", dest ,"-classpath", CLASSPATH.to_cp, *files
  end
end

def compile_scala(dest, *srcdirs)
  files = FileList.new
  srcdirs.each do |d|
    files.include("#{d}/**/*.scala")
  end

  sh "scalac", "-g", "-bootclasspath", BOOTCLASSPATH.to_cp,  "-nowarn",
     "-sourcepath", srcdirs.join(File::PATH_SEPARATOR), "-d", dest ,"-classpath", CLASSPATH.to_cp, *files
end

def spec_classes
  class_list = FileList.new.include(File.expand_path("#{@classes}/#{@spec_pkg.gsub(/\./, '/')}/**/*.class"))
  classes = []
  class_list.each do |f| 
    path = f.split(/\//)
    next unless path.last =~ /^\w+\.class$/
    path.push path.pop.gsub(/\.class/, "")
    classes << path[path.index("classes")+1..-1].join(".")
  end
  classes
end

task :default => :test

task :resource_src => dirs do
  sh "aapt package -m -J #{gen} -M AndroidManifest.xml -S #{res} -I #{android_jar}"
end

task :test => :compile do
  spec_classes.each do |klass|
    sh "java", "-cp", CLASSPATH.to_cp + ":#{File.expand_path(@classes)}", klass
  end
end


task :aidl => dirs do
  FileList["#{src}/**/*.aidl"].each do |f|
    sh "aidl -p #{android_aidl} -I #{src} -o #{gen} #{f}"
 end
end

task :compile => [:resource_src, :aidl] do
  compile_java(classes, gen)
  compile_scala(classes, src, gen)
end

task :shake => [ :compile ] do
  sh "java", "-Xmx512M", "-jar", "proguard/proguard.jar",
    "-injars", "#{classes}:#{scala_jars.last}(!META-INF/MANIFEST.MF,!library.properties)",
    "-outjars", "#{bin}/out.min.jar",
    "-libraryjars", android_jar,
    "-dontwarn",
    "-dontoptimize",
    "-dontobfuscate",
    "-keep",  "public class * extends android.app.Activity"
end

task :dex => :shake do
  sh "dx", *["-JXmx512M",  "--dex", "--output=#{intermediate_dex_location}", "#{bin}/out.min.jar"]
end

task :package_resources do
  opts = ["package", "-f", "-M", "AndroidManifest.xml", "-I", android_jar, "-S", res, "-F", ap_]
  opts += ["-A", assets] if File.directory?(assets)
  sh "aapt", *opts
end

apkbuilder = Proc.new do |signed| 
  args = [apk, "-f", intermediate_dex_location, "-rf", src, "-z", ap_]
  args += [ "-rj", libs] if File.directory?(libs)
  args += ["-u" ] unless signed

  sh "apkbuilder", *args
end

desc "Builds the application and sign it with a debug key."
task :debug => [:dex, :package_resources] do
  apkbuilder.call(true)
end

desc "Builds the application and sign it with a release key."
task :release => [:dex, :package_resources] do
  apkbuilder.call(false)
  Rake::Task['package:sign'].invoke
end

def adb(*args)
  args.unshift '-s', ENV['DEVICE'] if ENV['DEVICE']
  sh "adb", *args 
end

desc "Installs the debug package onto a running emulator or device (DEVICE=<serialno>)."
task :install => :debug do
  adb 'install', apk
end

desc "Installs the debug package on a running emulator or device that already has the application (DEVICE=<serialno>)."
task :reinstall => :debug do
  adb 'install', '-r', apk
end

desc "uninstall the application from a running emulator or device (DEVICE=<serialno>)."
task :uninstall do
  adb 'uninstall',  app_pkg
end

namespace :package do
  desc "verify signature of the package."
  task :verify do
     sh "jarsigner",  "-verify",  "-certs", "-verbose", apk
  end

  desc "sign the package."
  task :sign do
    alias_name = "alias"
    keystore   = "keystore"
    sh "jarsigner",  "-verbose",  "-keystore", keystore, apk, alias_name
  end
end


